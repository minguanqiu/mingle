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
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Custom springdoc for Svc
 *
 * @author Ming
 */

@Configuration
public class SvcDocConfig {

    @Autowired
    private SvcBinderComponent svcBinderComponent;
    @Autowired
    private SvcResModelHandler svcResModelHandler;
    @Value("${mingle.svc.openapi.useFqn:false}")
    public boolean useFqn;

    @Bean
    public GroupedOpenApi svcGroup() {
        return GroupedOpenApi.builder()
                .group("Service")
                .addOpenApiCustomiser(svcOpenApiCustomiser())
                .build();
    }

    @Bean
    public OpenApiCustomiser svcOpenApiCustomiser() {
        return openApi -> {
            ModelConverters instance = ModelConverters.getInstance();
            if (useFqn) {
                buildConverter(instance);
            }
            svcBinderComponent.getSvcMap().forEach((k, v) -> {
                PathItem pathItem = openApi.getPaths().get(v.getSvcPath());
                if (pathItem != null) {
                    Operation operation = null;
                    if (pathItem.getGet() != null) {
                        operation = pathItem.getGet();
                    } else if (pathItem.getPut() != null) {
                        operation = pathItem.getPut();
                    } else if (pathItem.getPost() != null) {
                        operation = pathItem.getPost();
                    } else if (pathItem.getDelete() != null) {
                        operation = pathItem.getDelete();
                    } else if (pathItem.getOptions() != null) {
                        operation = pathItem.getOptions();
                    } else if (pathItem.getHead() != null) {
                        operation = pathItem.getHead();
                    } else if (pathItem.getPatch() != null) {
                        operation = pathItem.getPatch();
                    } else if (pathItem.getTrace() != null) {
                        operation = pathItem.getTrace();
                    }
                    if (operation != null) {
                        buildPathItem(instance, openApi, operation, v);
                    }
                }
            });
        };
    }

    private void buildPathItem(ModelConverters instance, OpenAPI openApi, Operation operation, SvcBinderComponent.SvcBinderModel v) {
        operation.setTags(Arrays.asList(v.getSvc().tags()));
        operation.setSummary(v.getSvc().summary());
        operation.setDescription(v.getSvc().desc());
        if (!v.isReqCustom()) {
            ResolvedSchema reqModelSchema = instance.readAllAsResolvedSchema(v.getReqModelClass());
            operation.setRequestBody(new RequestBody()
                    .content(new Content()
                            .addMediaType("application/json", new MediaType().schema(reqModelSchema.schema))));
            if (operation.getParameters() != null) {
                operation.getParameters().clear();
            }
            openApi.schema(reqModelSchema.schema.getName(), reqModelSchema.schema);
            buildSchema(openApi, reqModelSchema.referencedSchemas);
        }
        if (!v.isResCustom()) {
            ResolvedSchema resModelSchema = instance.readAllAsResolvedSchema(v.getResModelClass());
            ResolvedSchema mainSvcResSchema = buildMainSvcResSchema(instance, v.getResModelClass());
            operation.setResponses(new ApiResponses()
                    .addApiResponse("200", new ApiResponse()
                            .description("successful operation")
                            .content(new Content()
                                    .addMediaType("application/json", new MediaType().schema(mainSvcResSchema.schema)))));
            openApi.schema(resModelSchema.schema.getName(), resModelSchema.schema);
            buildSchema(openApi, mainSvcResSchema.referencedSchemas);
            buildSchema(openApi, resModelSchema.referencedSchemas);
        }
    }

    private void buildConverter(ModelConverters instance) {
        ModelConverter modelConverter = null;
        for (ModelConverter converter : instance.getConverters()) {
            modelConverter = converter;
        }
        instance.removeConverter(modelConverter);
        instance.addConverter(new ModelResolver(Json.mapper(), new SvcDocTypeNameResolver(true)));
    }

    private void buildSchema(OpenAPI openAPI, Map<String, Schema> schemaMap) {
        schemaMap.forEach(openAPI::schema);
    }

    private ResolvedSchema buildMainSvcResSchema(ModelConverters instance, Class<?> clazz) {
        ResolvedSchema mainSvcResModel = instance.readAllAsResolvedSchema(svcResModelHandler.getClass());
        Schema<?> schema = new Schema<>();
        schema.name(clazz.getName());
        schema.$ref("#/components/schemas/" + clazz.getSimpleName());
        for (Method method : svcResModelHandler.getClass().getMethods()) {
            if (method.getName().equals("getResBody")) {
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

    @PostConstruct
    private void init() {
        ArrayList<Class<?>> classes = new ArrayList<>();
        svcBinderComponent.getSvcMap().forEach((k, v) -> {
            classes.add(v.getSvcClass());
        });
        AbstractOpenApiResource.addRestControllers(classes.toArray(new Class[0]));
    }

}
