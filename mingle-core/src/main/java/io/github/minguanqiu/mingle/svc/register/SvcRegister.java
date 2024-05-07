package io.github.minguanqiu.mingle.svc.register;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.minguanqiu.mingle.svc.AbstractService;
import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.exception.MingleRuntimeException;
import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

/**
 * Register service and check rules
 *
 * @author Ming
 */
@Slf4j
public class SvcRegister {

    @Getter
    private final SvcProperties svcProperties;
    private final List<Service<?, ?>> services;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final SvcPathHandler svcPathHandler;
    private final List<SvcFeatureRegister<?>> svcFeatureRegisters;
    @Getter
    private Map<String, SvcDefinition> svcDefinitionMap = new HashMap<>();

    public SvcRegister(SvcProperties svcProperties, List<Service<?, ?>> services, RequestMappingHandlerMapping requestMappingHandlerMapping, SvcPathHandler svcPathHandler, List<SvcFeatureRegister<?>> svcFeatureRegisters) {
        this.svcProperties = svcProperties;
        this.services = services;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.svcPathHandler = svcPathHandler;
        this.svcFeatureRegisters = svcFeatureRegisters;
        init();
    }

    public Optional<SvcDefinition> getSvcDefinition(HttpServletRequest request) {
        return getSvcDefinition(request.getServletPath());
    }

    public Optional<SvcDefinition> getSvcDefinition(String path) {
        return Optional.ofNullable(svcDefinitionMap.get(path));
    }

    public String[] getSvcPath(Predicate<SvcDefinition> predicate) {
        ArrayList<String> strings = new ArrayList<>();
        svcDefinitionMap.forEach((k, v) -> {
            if (predicate.test(v)) {
                strings.add(k);
            }
        });
        return strings.toArray(new String[0]);
    }

    public ArrayList<SvcDefinition> getSvcDefinition(Predicate<SvcDefinition> predicate) {
        ArrayList<SvcDefinition> svcDefinitions = new ArrayList<>();
        svcDefinitionMap.forEach((k, v) -> {
            if (predicate.test(v)) {
                svcDefinitions.add(v);
            }
        });
        return svcDefinitions;
    }

    private void register() {
        HashMap<String, ArrayList<String>> svcMissingMap = new HashMap<>();
        if (services == null) {
            throw new MingleRuntimeException("must create at least one service");
        }
        services.forEach(service -> {
            var abstractService = (AbstractService<?, ?>) service;
            Class<?> serviceClass = abstractService.getClass();
            SvcDefinition svcDefinition = new SvcDefinition();
            ArrayList<String> svcErrorMsgList = new ArrayList<>();
            Svc svc = AnnotatedElementUtils.findMergedAnnotation(serviceClass, Svc.class);
            if (svc != null) {
                svcDefinition.setService(service);
                svcDefinition.setSvcClass(serviceClass);
                svcDefinition.setSvcName(serviceClass.getSimpleName());
                svcDefinition.setSvc(svc);
                svcDefinition.setRequestClass(abstractService.getReqClass());
                svcDefinition.setResponseClass(abstractService.getResClass());
                buildServiceMethod(svcDefinition, svcErrorMsgList);
                svcDefinition.setSvcPath(svcPathHandler.getPath(serviceClass));
                registerFeature(svcDefinition);
            } else {
                svcErrorMsgList.add("missing @Svc");
            }
            if (!svcErrorMsgList.isEmpty()) {
                svcMissingMap.put(serviceClass.getSimpleName(), svcErrorMsgList);
            }
            svcDefinitionMap.put(svcDefinition.getSvcPath(), svcDefinition);
        });
        if (!svcMissingMap.isEmpty()) {
            try {
                throw new MingleRuntimeException(new ObjectMapper().writeValueAsString(svcMissingMap));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        svcDefinitionMap = Collections.unmodifiableMap(svcDefinitionMap);
        getSvcDefinitionMap().forEach((k, v) -> {
            registerServiceForMapping(v);
        });
    }

    private void buildServiceMethod(SvcDefinition svcDefinition, ArrayList<String> svcErrorMsgList) {
        Class<?> serviceClass = svcDefinition.getSvcClass();
        List<Method> methods = Arrays.stream(serviceClass.getDeclaredMethods()).filter(m -> AnnotationUtils.findAnnotation(m, RequestMapping.class) != null).toList();
        if (methods.size() > 1) {
            svcErrorMsgList.add("only allow one API entrance");
        }
        Method doService;
        try {
            doService = svcDefinition.getSvcClass().getMethod("doService", svcDefinition.getRequestClass());
        } catch (NoSuchMethodException e) {
            svcErrorMsgList.add("not found doService method");
            return;
        }
        if (AnnotationUtils.findAnnotation(serviceClass, ResponseBody.class) != null) {
            svcErrorMsgList.add("can not add @ResponseBody");
        }

        if (AnnotationUtils.findAnnotation(doService, RequestMapping.class) != null) {
            svcErrorMsgList.add("doService method can not add @RequestMapping");
        }
        if (AnnotationUtils.findAnnotation(doService, ResponseBody.class) != null) {
            svcErrorMsgList.add("doService method can not add @ResponseBody");
        }
        svcDefinition.setSvcMethod(doService);
    }

    private void registerServiceForMapping(SvcDefinition v) {
        requestMappingHandlerMapping
                .registerMapping(RequestMappingInfo.paths(v.getSvcPath())
                        .methods(RequestMethod.POST)
                        .consumes(MediaType.APPLICATION_JSON_VALUE)
                        .produces(MediaType.APPLICATION_JSON_VALUE)
                        .options(requestMappingHandlerMapping.getBuilderConfiguration()).build(), v.getService(), v.getSvcMethod());
    }

    private void registerFeature(SvcDefinition svcDefinition) {
        svcFeatureRegisters.forEach(svcFeatureRegister -> {
            if (svcFeatureRegister.support(svcDefinition)) {
                Object o = svcFeatureRegister.registerFeature(svcDefinition);
                svcDefinition.getFeatures().put(o.getClass(), o);
            }
        });
    }

    private void init() {
        register();
        log.info("servlet path : {}", Arrays.toString(getSvcPath((svcBinderModel -> true))));
        log.info("{} init", this.getClass().getSimpleName());
    }

    @Getter
    @Setter(AccessLevel.PACKAGE)
    public static class SvcDefinition {

        private String svcName;

        private String svcPath;

        @Getter(AccessLevel.PACKAGE)
        private Service<?, ?> service;

        private Class<?> svcClass;

        private Class<?> requestClass;

        private Class<?> responseClass;

        @Getter(AccessLevel.PACKAGE)
        private Method svcMethod;

        private Svc svc;

        @Getter(AccessLevel.PRIVATE)
        @Setter(AccessLevel.PRIVATE)
        private Map<Class<?>, Object> features = new HashMap<>();

        @SuppressWarnings("unchecked")
        public <T> Optional<T> getFeature(Class<T> feature) {
            return (Optional<T>) Optional.ofNullable(features.get(feature));
        }


    }

}
