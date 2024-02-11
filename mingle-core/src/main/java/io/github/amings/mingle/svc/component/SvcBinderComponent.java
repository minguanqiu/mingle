package io.github.amings.mingle.svc.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amings.mingle.svc.AbstractSvcLogic;
import io.github.amings.mingle.svc.annotation.Svc;
import io.github.amings.mingle.svc.configuration.properties.SvcProperties;
import io.github.amings.mingle.svc.exception.MingleRuntimeException;
import io.github.amings.mingle.utils.ReflectionUtils;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Predicate;

/**
 * Svc Binder Configuration
 *
 * @author Ming
 */

@Slf4j
@Component
public class SvcBinderComponent {

    private final SvcProperties svcProperties;
    private final List<AbstractSvcLogic<?, ?>> abstractSvcLogics;
    private final ApplicationContext context;
    @Getter
    private Map<String, SvcBinderModel> svcBinderModelMap = new HashMap<>();

    public SvcBinderComponent(SvcProperties svcProperties, List<AbstractSvcLogic<?, ?>> abstractSvcLogics, ApplicationContext applicationContext) {
        this.svcProperties = svcProperties;
        this.abstractSvcLogics = abstractSvcLogics;
        this.context = applicationContext;
    }

    public Optional<SvcBinderModel> getSvcBinderModel(String path) {
        return Optional.ofNullable(svcBinderModelMap.get(path));
    }

    private void scanSvc() {
        HashMap<String, ArrayList<String>> svcMissingMap = new HashMap<>();
        if (abstractSvcLogics == null) {
            throw new MingleRuntimeException("you must create at least one Service");
        }
        abstractSvcLogics.forEach(svcLogic -> {
            Class<?> clazz = svcLogic.getClass();
            SvcBinderModel svcBinderModel = new SvcBinderModel();
            ArrayList<String> svcErrorMsgList = new ArrayList<>();
            Svc svc = AnnotatedElementUtils.findMergedAnnotation(clazz, Svc.class);
            if (svc != null) {
                svcBinderModel.setSvcClass(clazz);
                svcBinderModel.setSvcName(clazz.getSimpleName());
                svcBinderModel.setSvc(svc);
                List<Method> methods = Arrays.stream(clazz.getDeclaredMethods()).filter(m -> AnnotationUtils.findAnnotation(m, RequestMapping.class) != null).toList();
                if (methods.size() > 1) {
                    svcErrorMsgList.add("only allow one API entrance");
                }
                ParameterizedType parameterizedType = (ParameterizedType) ReflectionUtils.findGenericSuperclass(clazz, AbstractSvcLogic.class);
                Class<?> reqModelClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                Class<?> resModelClass = (Class<?>) parameterizedType.getActualTypeArguments()[1];
                svcBinderModel.setReqModelClass(reqModelClass);
                svcBinderModel.setResModelClass(resModelClass);
                Method doService = null;
                try {
                    doService = clazz.getMethod("doService", reqModelClass, resModelClass);
                } catch (NoSuchMethodException ignored) {

                }
                if (AnnotationUtils.findAnnotation(clazz, ResponseBody.class) != null) {
                    svcErrorMsgList.add("Svc class can not add @ResponseBody");
                }
                if (AnnotationUtils.findAnnotation(doService, RequestMapping.class) != null) {
                    svcErrorMsgList.add("doService method can not add @RequestMapping");
                }
                if (AnnotationUtils.findAnnotation(doService, ResponseBody.class) != null) {
                    svcErrorMsgList.add("doService method can not add @ResponseBody");
                }
                String svcPathPrefix = svcProperties.getRootPath();
                if (!svc.path().isEmpty()) {
                    String path = svc.path();
                    if (!path.startsWith("/")) {
                        path += "/" + path;
                    }
                    svcPathPrefix += path;
                }
                String svcPath = svcPathPrefix + "/" + clazz.getSimpleName();
                svcBinderModel.setSvcMethod(doService);
                svcBinderModel.setSvcPath(svcPath);
                svcBinderModel.setMethod(RequestMethod.POST);

                if (svc.ipSecure()) {
                    String property = context.getEnvironment().getProperty("mingle.svc.security.ip." + clazz.getSimpleName());
                    if (property != null) {
                        svcBinderModel.setIpSecureList(property.split(","));
                    } else {
                        svcErrorMsgList
                                .add("ipSecure if true，must set " + "mingle.svc.security.ip." + clazz.getSimpleName());
                    }
                }
            } else {
                svcErrorMsgList.add("missing @Svc");
            }
            if (!svcErrorMsgList.isEmpty()) {
                svcMissingMap.put(clazz.getSimpleName(), svcErrorMsgList);
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

    @PostConstruct
    private void init() {
        scanSvc();
        log.info("SvcPathList : " + Arrays.toString(findSvcPath(svcBinderModel -> true)));
        log.info(this.getClass().getSimpleName() + " " + "Init success");
    }

    @Getter
    @Setter(AccessLevel.PACKAGE)
    public static class SvcBinderModel {

        private Class<?> svcClass;

        private String svcName;

        private String svcPath;

        private RequestMethod method;

        private Method svcMethod;

        private Svc svc;

        private boolean custom; // method custom

        private boolean reqCustom;

        private boolean resCustom;

        private Class<?> reqModelClass;

        private Class<?> resModelClass;

        private String[] ipSecureList;

    }

}
