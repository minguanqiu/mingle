package io.github.amings.mingle.svc.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amings.mingle.svc.AbstractSvcLogic;
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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @Autowired
    List<AbstractSvcLogic> abstractSvcLogics;
    private Map<Class<?>, SvcBinderModel> svcBinderModelMap = new HashMap<>();
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

    public Optional<SvcBinderModel> getSvcBinderModel(Class<?> clazz) {
        return Optional.ofNullable(svcBinderModelMap.get(clazz));
    }

    public Map<Class<?>, SvcBinderModel> getSvcBinderModelMap() {
        return svcBinderModelMap;
    }

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
        if (abstractSvcLogics.size() == 0) {
            throw new MingleRuntimeException("must create at least one service");
        }
        abstractSvcLogics.forEach(svcLogic -> {
            Class<? extends AbstractSvcLogic> clazz = svcLogic.getClass();
            SvcBinderModel svcBinderModel = new SvcBinderModel();
            ArrayList<String> svcMissingList = new ArrayList<>();
            Svc svc = clazz.getAnnotation(Svc.class);
            if (svc != null) {
                Class<?> reqClass = svcLogic.getReqClass();
                Class<?> resClass = svcLogic.getResClass();
                svcBinderModel.setSvc(svc);
                svcBinderModel.setSvcName(clazz.getSimpleName());
                svcBinderModel.setReqModelClass(reqClass);
                svcBinderModel.setResModelClass(resClass);
                svcBinderModel.setCustom(svc.custom());
                Method method = null;
                try {
                    method = clazz.getMethod("doService", reqClass, resClass);
                } catch (NoSuchMethodException ignored) {
                }
                String svcPathPrefix = webConfig.getSvcPath();
                if (!svc.path().equals("")) {
                    svcPathPrefix = svc.path();
                }
                String svcPath = svcPathPrefix + "/" + clazz.getSimpleName();
                if (!svcBinderModel.isCustom()) {
                    requestMappingHandlerMapping
                            .registerMapping(RequestMappingInfo.paths(svcPath)
                                    .methods(RequestMethod.POST)
                                    .consumes(MediaType.APPLICATION_JSON_VALUE)
                                    .produces(MediaType.APPLICATION_JSON_VALUE)
                                    .options(requestMappingHandlerMapping.getBuilderConfiguration()).build(), context.getBean(clazz), method);
                    svcBinderModel.setSvcPath(svcPath);
                }

                if (svc.ipAddressSecure()) {
                    String property = context.getEnvironment().getProperty("mingle.svc.security.ip." + clazz.getSimpleName());
                    if (property != null) {
                        ipAddressMap.put(svcBinderModel.getSvcPath(), buildIpAddressPattern(property));
                    } else {
                        svcMissingList
                                .add("ipAddressSecure is true，must set " + "mingle.svc.security.ip." + clazz.getSimpleName());
                    }
                }
            } else {
                svcMissingList.add("missing @Svc");
            }
            if (!svcMissingList.isEmpty()) {
                svcMissingMap.put(clazz.getSimpleName(), svcMissingList);
            }

            svcBinderModelMap.put(clazz, svcBinderModel);
        });
        if (!svcMissingMap.isEmpty()) {
            try {
                throw new MingleRuntimeException(new ObjectMapper().writeValueAsString(svcMissingMap));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        svcBinderModelMap = Collections.unmodifiableMap(svcBinderModelMap);
        ipAddressMap = Collections.unmodifiableMap(ipAddressMap);
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
            if (!v.getSvc().custom()) {
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

        private String svcName;

        private String svcPath;

        private Svc svc;

        private boolean custom;

        private Class<?> reqModelClass;

        private Class<?> resModelClass;

    }

}
