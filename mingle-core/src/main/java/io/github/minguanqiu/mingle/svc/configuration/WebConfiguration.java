package io.github.minguanqiu.mingle.svc.configuration;

import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import io.github.minguanqiu.mingle.svc.filter.SvcRequestResponseBodyMethodProcessor;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for spring web
 *
 * @author Qiu Guan Ming
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  private final SvcInfo svcInfo;
  private final JacksonUtils jacksonUtils;

  /**
   * Create a new WebConfiguration instance.
   *
   * @param svcInfo      the service information.
   * @param jacksonUtils the jackson utils.
   */
  public WebConfiguration(SvcInfo svcInfo, JacksonUtils jacksonUtils) {
    this.svcInfo = svcInfo;
    this.jacksonUtils = jacksonUtils;
  }

  /**
   * Add argument resolvers
   */
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(svcRequestResponseBodyMethodProcessor());
  }

  /**
   * Add return value handlers
   */
  @Override
  public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
    handlers.add(svcRequestResponseBodyMethodProcessor());
  }

  /**
   * Create a new SvcRequestResponseBodyMethodProcessor instance.
   *
   * @return return the service request and response body processor.
   */
  @Bean
  public SvcRequestResponseBodyMethodProcessor svcRequestResponseBodyMethodProcessor() {
    List<HttpMessageConverter<?>> converters = new ArrayList<>();
    converters.add(new MappingJackson2HttpMessageConverter(jacksonUtils.getObjectMapper()));
    return new SvcRequestResponseBodyMethodProcessor(converters, svcInfo);
  }


}
