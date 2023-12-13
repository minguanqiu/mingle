package io.github.amings.mingle.svc.session.component;

import io.github.amings.mingle.svc.component.SvcBinderComponent;
import io.github.amings.mingle.svc.session.annotation.Session;
import io.github.amings.mingle.utils.UUIDUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Session binder component
 *
 * @author Ming
 */

@Slf4j
@Component
public class SessionBinderComponent {

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    SvcBinderComponent svcBinderComponent;
    private List<String> sessionPathList = new ArrayList<>();
    private Map<String, String> authorityMap = new HashMap<>();
    private Map<String, String> sessionMap = new HashMap<>();

    public List<String> getSessionPathList() {
        return sessionPathList;
    }
    public Map<String, String> getAuthorityMap() {
        return authorityMap;
    }
    public Map<String, String> getSessionMap() {
        return sessionMap;
    }

    private void buildBinder() {
        svcBinderComponent.getSvcMap().forEach((k, v) -> {
            Session session = v.getSvcClass().getAnnotation(Session.class);
            if (session != null) {
                sessionPathList.add(v.getSvcPath());
                sessionMap.put(v.getSvcPath(), session.value());
                if (session.authority()) {
                    authorityMap.put(v.getSvcPath(), v.getSvcName());
                }
            }
        });
        if(sessionPathList.isEmpty()) {
            sessionPathList.add("/" + UUIDUtils.generateUuid());
        }
        sessionPathList = Collections.unmodifiableList(sessionPathList);
        authorityMap = Collections.unmodifiableMap(authorityMap);
        sessionMap = Collections.unmodifiableMap(sessionMap);
    }

    @PostConstruct
    private void init() {
        buildBinder();
        log.info("SessionPaths : " + sessionPathList);
        log.info(this.getClass().getSimpleName() + " " + "Init success");
    }

}
