package io.github.amings.mingle.svc.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amings.mingle.svc.AbstractSvcLogic;
import io.github.amings.mingle.svc.SvcReqModel;
import io.github.amings.mingle.svc.SvcResModel;
import io.github.amings.mingle.svc.annotation.Svc;
import io.github.amings.mingle.svc.config.WebConfig;
import io.github.amings.mingle.svc.exception.MingleRuntimeException;
import io.github.amings.mingle.utils.JacksonUtils;
import io.github.amings.mingle.utils.UUIDUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Svc Binder Configuration
 *
 * @author Ming
 */

@Slf4j
@Component
public class SvcBinderComponent {

    @Autowired
    WebConfig webConfig;
    @Autowired
    JacksonUtils jacksonUtils;
    @Autowired(required = false)
    List<AbstractSvcLogic> abstractSvcLogics;
    private Map<String, SvcBinderModel> svcBinderModelMap = new HashMap<>();

    private Map<String, String> ipAddressMap = new HashMap<>();
    private List<String> svcPathList;
    private List<String> svcValidBeanPathList;
    private List<String> svcLogPathList;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private ApplicationContext context;

    public String getMainPackage() {
        return AutoConfigurationPackages.get(context.getAutowireCapableBeanFactory()).get(0);
    }

    @Deprecated
    public Optional<SvcBinderModel> getSvcBinderModel(Class<?> clazz) {
        return Optional.empty();
    }

    @Deprecated
    public Map<Class<?>, SvcBinderModel> getSvcBinderModelMap() {
        return null;
    }

    public Optional<SvcBinderModel> getSvcBinderModel(String path) {
        return Optional.ofNullable(svcBinderModelMap.get(path));
    }

    public Map<String, SvcBinderModel> getSvcMap() {
        return svcBinderModelMap;
    }

    @Deprecated
    public Map<String, String> getIpAddressMap() {
        return ipAddressMap;
    }

    public List<String> getSvcPathList() {
        return svcPathList;
    }

    public List<String> getSvcValidBeanPathList() {
        return svcValidBeanPathList;
    }

    public List<String> getSvcLogPathList() {
        return svcLogPathList;
    }

    private void scanSvc() {
        HashMap<String, ArrayList<String>> svcMissingMap = new HashMap<>();
        if (abstractSvcLogics == null) {
            throw new MingleRuntimeException("you must create at least one Service");
        }
        abstractSvcLogics.forEach(svcLogic -> {
            Class<? extends AbstractSvcLogic> clazz = svcLogic.getClass();
            SvcBinderModel svcBinderModel = new SvcBinderModel();
            ArrayList<String> svcErrorMsgList = new ArrayList<>();
            Svc svc = AnnotatedElementUtils.findMergedAnnotation(clazz, Svc.class);
            if (svc != null) {
                Class<?> reqClass = svcLogic.getReqClass();
                Class<?> resClass = svcLogic.getResClass();
                svcBinderModel.setSvcClass(clazz);
                svcBinderModel.setSvcName(clazz.getSimpleName());
                svcBinderModel.setSvc(svc);
                svcBinderModel.setReqModelClass(reqClass);
                svcBinderModel.setResModelClass(resClass);
                List<Method> methods = Arrays.stream(clazz.getDeclaredMethods()).filter(m -> AnnotationUtils.findAnnotation(m, RequestMapping.class) != null).collect(Collectors.toList());
                if (methods.size() > 1) {
                    svcErrorMsgList.add("only allow one API entrance");
                } else if (methods.size() == 1) {
                    svcBinderModel.setCustom(true);
                    Method method = methods.get(0);
                    RequestMapping annotation = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
                    RequestMethod[] requestMethod = annotation.method();
                    if (requestMethod.length == 1) {
                        svcBinderModel.setMethod(annotation.method()[0]);
                    } else if (requestMethod.length > 1) {
                        svcErrorMsgList.add("custom request method can not multiple");
                    } else {
                        svcErrorMsgList.add("custom request method can not empty");
                    }
                    boolean hasReqClass = false;
                    for (Class<?> parameterType : method.getParameterTypes()) {
                        if (SvcReqModel.class.isAssignableFrom(parameterType)) {
                            if (!reqClass.equals(parameterType)) {
                                svcErrorMsgList.add("custom method parameter must defined " + reqClass.getSimpleName() + " request class");
                            } else {
                                hasReqClass = true;
                            }
                        }
                    }
                    if (!hasReqClass) {
                        svcBinderModel.setReqCustom(true);
                    }
                    if (SvcResModel.class.isAssignableFrom(method.getReturnType())) {
                        if (!resClass.equals(method.getReturnType())) {
                            svcErrorMsgList.add("custom method returnType must defined " + resClass.getSimpleName() + " response class");
                        }
                    } else {
                        svcBinderModel.setResCustom(true);
                    }
                    String[] path = annotation.path().length == 0 ? annotation.value() : annotation.path();
                    if (path.length == 1) {
                        svcBinderModel.setSvcPath(annotation.path()[0]);
                    } else if (path.length > 1) {
                        svcErrorMsgList.add("custom path can not multiple");
                    } else {
                        svcErrorMsgList.add("custom path can not empty");
                    }
                }
                Method doService = null;
                try {
                    doService = clazz.getMethod("doService", reqClass, resClass);
                } catch (NoSuchMethodException ignored) {
                }
                if (AnnotationUtils.findAnnotation(doService, RequestMapping.class) != null) {
                    svcErrorMsgList.add("doService method can not add @RequestMapping");
                }
                if (!svcBinderModel.isCustom()) {
                    String svcPathPrefix = webConfig.getSvcPath();
                    if (!svc.path().equals("")) {
                        String path = svc.path();
                        if (!path.startsWith("/")) {
                            path += "/" + path;
                        }
                        svcPathPrefix += path;
                    }
                    String svcPath = svcPathPrefix + "/" + clazz.getSimpleName();
                    requestMappingHandlerMapping
                            .registerMapping(RequestMappingInfo.paths(svcPath)
                                    .methods(RequestMethod.POST)
                                    .consumes(MediaType.APPLICATION_JSON_VALUE)
                                    .produces(MediaType.APPLICATION_JSON_VALUE)
                                    .options(requestMappingHandlerMapping.getBuilderConfiguration()).build(), context.getBeanNamesForType(clazz)[0], doService);
                    svcBinderModel.setSvcPath(svcPath);
                    svcBinderModel.setMethod(RequestMethod.POST);
                }

                if (svc.ipAddressSecure()) {
                    String property = context.getEnvironment().getProperty("mingle.svc.security.ip." + clazz.getSimpleName());
                    if (property != null) {
                        svcBinderModel.setIpSecure(property);
                    } else {
                        svcErrorMsgList
                                .add("ipAddressSecure is true，must set " + "mingle.svc.security.ip." + clazz.getSimpleName());
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

    public String buildIpAddressPattern(String property) {
        StringBuilder ipString = new StringBuilder();
        if (!property.contains(",")) {
            ipString.append("hasIpAddress('")
                    .append(property)
                    .append("')");
            return ipString.toString();
        } else {
            String[] ipAddressProperties = property.split(",");
            for (String ip : ipAddressProperties) {
                ipString.append("hasIpAddress('")
                        .append(ip)
                        .append("') or ");
            }
            return ipString.substring(0, ipString.length() - 4);
        }
    }


    private void buildSvcPathList() {
        ArrayList<String> svcPathList = new ArrayList<>();
        ArrayList<String> svcValidBeanPathList = new ArrayList<>();
        ArrayList<String> svcLogPathList = new ArrayList<>();
        svcBinderModelMap.forEach((k, v) -> {
            svcPathList.add(v.getSvcPath());
            if (v.getSvc().log()) {
                svcLogPathList.add(v.getSvcPath());
            }
            if (!v.isReqCustom()) {
                svcValidBeanPathList.add(v.getSvcPath());
            }
        });
        if (svcLogPathList.isEmpty()) {
            svcLogPathList.add("/" + UUIDUtils.generateUuid());
        }
        if (svcValidBeanPathList.isEmpty()) {
            svcValidBeanPathList.add("/" + UUIDUtils.generateUuid());
        }
        this.svcPathList = Collections.unmodifiableList(svcPathList);
        this.svcValidBeanPathList = Collections.unmodifiableList(svcValidBeanPathList);
        this.svcLogPathList = Collections.unmodifiableList(svcLogPathList);
    }

    @PostConstruct
    private void init() {
        scanSvc();
        buildSvcPathList();
        log.info("SvcPathList : " + svcPathList);
        log.info("IpAddressSecurityMap : " + ipAddressMap);
        log.info(this.getClass().getSimpleName() + " " + "Init success");
    }

    @Getter
    @Setter(AccessLevel.PACKAGE)
    public static class SvcBinderModel {

        private Class<?> svcClass;

        private String svcName;

        private String svcPath;

        @Deprecated
        private PathPattern pathPattern;

        private RequestMethod method;

        private Svc svc;

        private boolean custom;

        private boolean reqCustom;

        private boolean resCustom;

        private Class<?> reqModelClass;

        private Class<?> resModelClass;

        private String ipSecure;

    }

}
