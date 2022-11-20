package io.github.amings.mingle.svc.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.amings.mingle.svc.component.SvcBinderComponent;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Custom springdoc for Svc
 *
 * @author Ming
 */

@Configuration
public class SvcDocConfig {

    @Autowired
    ApplicationContext context;
    @Autowired
    SvcBinderComponent svcBinderComponent;
    @Autowired
    SvcResModelHandler svcResModelHandler;

    @Bean
    public GroupedOpenApi svcGroup() {
        return GroupedOpenApi.builder()
                .group("svc")
                .addOpenApiCustomiser(svcOpenApiCustomiser())
                .packagesToScan(AutoConfigurationPackages.get(context.getAutowireCapableBeanFactory()).get(0))
                .build();
    }

    @Bean
    public OpenApiCustomiser svcOpenApiCustomiser() {
        return openApi -> {
            openApi.info(new Info().title("OpenAPI definition").version("v1"));
            svcBinderComponent.getSvcBinderModelMap().forEach((k, v) -> {
                ModelConverters instance = ModelConverters.getInstance();
                ModelConverter modelConverter = null;
                for (ModelConverter converter : instance.getConverters()) {
                    modelConverter = converter;
                }
                instance.removeConverter(modelConverter);
                instance.addConverter(new ModelResolver(Json.mapper(), new SvcDocTypeNameResolver(true)));
                ResolvedSchema reqModelSchema = instance.readAllAsResolvedSchema(v.getReqModelClass());
                ResolvedSchema resModelSchema = instance.readAllAsResolvedSchema(v.getResModelClass());
                ResolvedSchema mainSvcResSchema = buildMainSvcResSchema(instance, v.getResModelClass());
                openApi.path(v.getSvcPath(),
                        new PathItem()
                                .post(new Operation()
                                        .summary(v.getSvc().desc())
                                        .addTagsItem("Svc")
                                        .operationId(v.getSvcPath())
                                        .requestBody(new RequestBody()
                                                .content(new Content()
                                                        .addMediaType("application/json", new MediaType().schema(reqModelSchema.schema))))
                                        .responses(new ApiResponses()
                                                .addApiResponse("200", new ApiResponse()
                                                        .description("successful operation")
                                                        .content(new Content()
                                                                .addMediaType("application/json", new MediaType().schema(mainSvcResSchema.schema)))))));
                openApi.schema(reqModelSchema.schema.getName(), reqModelSchema.schema);
                openApi.schema(reqModelSchema.schema.getName(), resModelSchema.schema);
                buildSchema(openApi, mainSvcResSchema.referencedSchemas);
                buildSchema(openApi, reqModelSchema.referencedSchemas);
                buildSchema(openApi, resModelSchema.referencedSchemas);
            });
        };
    }

    private void buildSchema(OpenAPI openAPI, Map<String, Schema> schemaMap) {
        schemaMap.forEach(openAPI::schema);
    }

    private ResolvedSchema buildMainSvcResSchema(ModelConverters instance, Class<?> clazz) {
        ResolvedSchema mainSvcResModel = instance.readAllAsResolvedSchema(svcResModelHandler.getClass());
        Schema<?> schema = new Schema<>();
        schema.name(clazz.getName());
        schema.$ref("#/components/schemas/" + clazz.getName());
        for (Method method : svcResModelHandler.getClass().getMethods()) {
            if(method.getName().equals("getResBody")) {
                String filedName = method.getAnnotation(JsonProperty.class).value();
                mainSvcResModel.schema.getProperties().remove("resBody");
                mainSvcResModel.schema.addProperty(filedName, schema);
            }
        }
        return mainSvcResModel;
    }

    protected static class SvcDocTypeNameResolver extends TypeNameResolver {
        private final boolean useFqn;

        public SvcDocTypeNameResolver(boolean useFqn) {
            this.useFqn = useFqn;
        }

        @Override
        protected String getNameOfClass(Class<?> cls) {
            return this.useFqn ? cls.getName().replace("$", "_") : cls.getSimpleName();
        }

    }

}
