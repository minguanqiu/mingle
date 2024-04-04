package io.github.amings.mingle.svc.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amings.mingle.svc.AbstractService;
import io.github.amings.mingle.svc.Service;
import io.github.amings.mingle.svc.annotation.Svc;
import io.github.amings.mingle.svc.configuration.properties.SvcProperties;
import io.github.amings.mingle.svc.exception.MingleRuntimeException;
import io.github.amings.mingle.svc.handler.SvcPathHandler;
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
 * Component for build service model and check rules
 *
 * @author Ming
 */
@Slf4j
public class SvcBinderComponent {

    @Getter
    private final SvcProperties svcProperties;
    private final List<Service<?, ?>> services;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final SvcPathHandler svcPathHandler;
    @Getter
    private Map<String, SvcBinderModel> svcBinderModelMap = new HashMap<>();

    public SvcBinderComponent(SvcProperties svcProperties, List<Service<?, ?>> services, RequestMappingHandlerMapping requestMappingHandlerMapping, SvcPathHandler svcPathHandler) {
        this.svcProperties = svcProperties;
        this.services = services;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.svcPathHandler = svcPathHandler;
        init();
    }

    public Optional<SvcBinderModel> getSvcBinderModel(String path) {
        return Optional.ofNullable(svcBinderModelMap.get(path));
    }

    public Optional<SvcBinderModel> getSvcBinderModel(HttpServletRequest request) {
        return getSvcBinderModel(request.getServletPath());
    }

    private void scanSvc() {
        HashMap<String, ArrayList<String>> svcMissingMap = new HashMap<>();
        if (services == null) {
            throw new MingleRuntimeException("you must create at least one Service");
        }
        services.forEach(service -> {
            var abstractService = (AbstractService<?, ?>) service;
            Class<?> serviceClass = abstractService.getClass();
            SvcBinderModel svcBinderModel = new SvcBinderModel();
            ArrayList<String> svcErrorMsgList = new ArrayList<>();
            Svc svc = AnnotatedElementUtils.findMergedAnnotation(serviceClass, Svc.class);
            if (svc != null) {
                svcBinderModel.setService(service);
                svcBinderModel.setSvcClass(serviceClass);
                svcBinderModel.setSvcName(serviceClass.getSimpleName());
                svcBinderModel.setSvc(svc);
                List<Method> methods = Arrays.stream(serviceClass.getDeclaredMethods()).filter(m -> AnnotationUtils.findAnnotation(m, RequestMapping.class) != null).toList();
                if (methods.size() > 1) {
                    svcErrorMsgList.add("only allow one API entrance");
                }
                Class<?> reqModelClass = abstractService.getReqClass();
                Class<?> resModelClass = abstractService.getResClass();
                svcBinderModel.setRequestClass(reqModelClass);
                svcBinderModel.setResponseClass(resModelClass);
                Method doService;
                try {
                    doService = serviceClass.getMethod("doService", reqModelClass);
                } catch (NoSuchMethodException e) {
                    svcErrorMsgList.add("not found doService method");
                    return;
                }
                if (AnnotationUtils.findAnnotation(serviceClass, ResponseBody.class) != null) {
                    svcErrorMsgList.add("Svc class can not add @ResponseBody");
                }

                if (AnnotationUtils.findAnnotation(doService, RequestMapping.class) != null) {
                    svcErrorMsgList.add("doService method can not add @RequestMapping");
                }
                if (AnnotationUtils.findAnnotation(doService, ResponseBody.class) != null) {
                    svcErrorMsgList.add("doService method can not add @ResponseBody");
                }
                svcBinderModel.setSvcMethod(doService);
                svcBinderModel.setSvcPath(svcPathHandler.getPath(serviceClass));
                svcBinderModel.setMethod(RequestMethod.POST);
            } else {
                svcErrorMsgList.add("missing @Svc");
            }
            if (!svcErrorMsgList.isEmpty()) {
                svcMissingMap.put(serviceClass.getSimpleName(), svcErrorMsgList);
            }
            svcBinderModelMap.put(svcBinderModel.getSvcPath(), svcBinderModel);
        });
        if (!svcMissingMap.isEmpty()) {
            try {
                throw new MingleRuntimeException(new ObjectMapper().writeValueAsString(svcMissingMap));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        svcBinderModelMap = Collections.unmodifiableMap(svcBinderModelMap);
        getSvcBinderModelMap().forEach((k, v) -> {
            registerServiceForMapping(v);
        });
    }

    public String[] findSvcPath(Predicate<SvcBinderModel> predicate) {
        ArrayList<String> strings = new ArrayList<>();
        svcBinderModelMap.forEach((k, v) -> {
            if (predicate.test(v)) {
                strings.add(k);
            }
        });
        return strings.toArray(new String[0]);
    }

    public ArrayList<SvcBinderModel> findSvcBinderModel(Predicate<SvcBinderModel> predicate) {
        ArrayList<SvcBinderModel> svcBinderModels = new ArrayList<>();
        svcBinderModelMap.forEach((k, v) -> {
            if (predicate.test(v)) {
                svcBinderModels.add(v);
            }
        });
        return svcBinderModels;
    }

    private void registerServiceForMapping(SvcBinderComponent.SvcBinderModel v) {
        requestMappingHandlerMapping
                .registerMapping(RequestMappingInfo.paths(v.getSvcPath())
                        .methods(RequestMethod.POST)
                        .consumes(MediaType.APPLICATION_JSON_VALUE)
                        .produces(MediaType.APPLICATION_JSON_VALUE)
                        .options(requestMappingHandlerMapping.getBuilderConfiguration()).build(), v.getService(), v.getSvcMethod());
    }
    private void init() {
        scanSvc();
        log.info("servlet path : " + Arrays.toString(findSvcPath((svcBinderModel -> true))));
        log.info(this.getClass().getSimpleName() + " " + "init");
    }

    @Getter
    @Setter(AccessLevel.PACKAGE)
    public static class SvcBinderModel {

        private Service<?,?> service;

        private Class<?> svcClass;

        private String svcName;

        private String svcPath;

        private RequestMethod method;

        private Method svcMethod;

        private Svc svc;

        private Class<?> requestClass;

        private Class<?> responseClass;

        private String[] ipSecureList;

    }

}
