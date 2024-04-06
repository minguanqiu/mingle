package io.github.minguanq.mingle.svc.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.minguanq.mingle.svc.component.SvcRegisterComponent;
import io.github.minguanq.mingle.svc.handler.SvcResponseHandler;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
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
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Configuration for service spring doc
 *
 * @author Ming
 */
@Configuration
public class SvcDocConfiguration {

    private final SvcRegisterComponent svcRegisterComponent;
    private final SvcResponseHandler svcResponseHandler;
    private String responseBodyFiledName;
    private final ModelConverters instance = ModelConverters.getInstance();

    public SvcDocConfiguration(SvcRegisterComponent svcRegisterComponent, SvcResponseHandler svcResponseHandler) {
        this.svcRegisterComponent = svcRegisterComponent;
        this.svcResponseHandler = svcResponseHandler;
        init();
    }

    @Bean
    public GroupedOpenApi svcGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("Service")
                .addOpenApiCustomizer(svcOpenApiCustomizer())
                .build();
    }

    @Bean
    public OpenApiCustomizer svcOpenApiCustomizer() {
        return openApi -> {
            svcRegisterComponent.getSvcDefinitionMap().forEach((k, v) -> {
                PathItem pathItem = openApi.getPaths().get(v.getSvcPath());
                if (pathItem != null) {
                    Operation operation = null;
                    if (pathItem.getGet() != null) {
                        operation = pathItem.getGet();
                    } else if (pathItem.getPut() != null) {
                        operation = pathItem.getPut();
                    } else if (pathItem.getPost() != null) {
                        operation = pathItem.getPost(); //svc only post method
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

    private void buildPathItem(ModelConverters instance, OpenAPI openApi, Operation operation, SvcRegisterComponent.SvcDefinition v) {
        operation.operationId(v.getSvcPath());
        operation.setTags(Arrays.asList(v.getSvc().tags()));
        operation.setSummary(v.getSvc().summary());
        operation.setDescription(v.getSvc().description());
        ResolvedSchema requestSchema = instance.readAllAsResolvedSchema(v.getRequestClass());
        operation.setRequestBody(new RequestBody()
                .content(new Content()
                        .addMediaType("application/json", new MediaType().schema(requestSchema.schema))));
        if (operation.getParameters() != null) {
            operation.getParameters().clear();
        }
        openApi.schema(requestSchema.schema.getName(), requestSchema.schema);
        buildSchema(openApi, requestSchema.referencedSchemas);
        ResolvedSchema responseSchema = instance.readAllAsResolvedSchema(v.getResponseClass());
        ResolvedSchema fullResponseSchema = buildMainSvcResSchema(instance, responseSchema);
        ApiResponses defaultApiResponses = buildResponse(fullResponseSchema);
        ApiResponses operationResponses = operation.getResponses();
        if (operationResponses.containsKey("200")) {
            operationResponses.get("200").content(new Content()
                    .addMediaType("application/json", new MediaType().schema(fullResponseSchema.schema)));
        } else {
            operationResponses.addApiResponse("200", defaultApiResponses.get("200"));
        }
        openApi.schema(responseSchema.schema.getName(), responseSchema.schema);
        buildSchema(openApi, responseSchema.referencedSchemas);
    }

    private void buildSchema(OpenAPI openAPI, Map<String, Schema> schemaMap) {
        schemaMap.forEach(openAPI::schema);
    }

    private ResolvedSchema buildMainSvcResSchema(ModelConverters instance, ResolvedSchema resModelSchema) {
        ResolvedSchema mainSvcResModel = instance.readAllAsResolvedSchema(svcResponseHandler.getClass());
        mainSvcResModel.schema.addProperty(responseBodyFiledName, resModelSchema.schema);
        return mainSvcResModel;
    }

    private ApiResponses buildResponse(ResolvedSchema resolvedSchema) {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                        .description("successful operation")
                        .content(new Content()
                                .addMediaType("application/json", new MediaType().schema(resolvedSchema.schema))));
    }

    private void init() {
        ArrayList<Class<?>> classes = new ArrayList<>();
        svcRegisterComponent.getSvcDefinitionMap().forEach((k, v) -> {
            classes.add(v.getSvcClass());
        });
        AbstractOpenApiResource.addRestControllers(classes.toArray(new Class[0]));
        for (Method method : svcResponseHandler.getClass().getDeclaredMethods()) {
            if (method.getName().equals("getResponseBody") && method.getReturnType().equals(JsonNode.class)) {
                this.responseBodyFiledName = method.getAnnotation(JsonProperty.class) != null ? method.getAnnotation(JsonProperty.class).value() : "responseBody";
            }
        }
    }

}
