package kr.sparta.rchive.global.config;

import io.swagger.annotations.Example;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import kr.sparta.rchive.global.execption.ApiExceptionCodeExample;
import kr.sparta.rchive.global.execption.ExceptionCode;
import kr.sparta.rchive.global.execption.ExceptionReason;
import kr.sparta.rchive.global.execption.ExceptionResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    private Docket testDocket(String groupName, Predicate<String> selector) {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .securityContexts(List.of(this.securityContext()))
                .securitySchemes(List.of(this.apiKey()))
                .groupName("testApi")
                .select()
                .apis(RequestHandlerSelectors.basePackage("kr.sparta.rchive"))
//                .paths(PathSelectors.ant("/api/v1/**"))
                .paths(PathSelectors.any())
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global",
                "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    // swagger token 인증 API
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "BearerAuth";

        return new OpenAPI()
                .info(new Info().title("Example API").version("v1"))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));

    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiExceptionCodeExample apiExceptionCodeExample =
                    handlerMethod.getMethodAnnotation(ApiExceptionCodeExample.class);
            // ApiErrorCodeExample 어노테이션 단 메소드 적용
            if (apiExceptionCodeExample != null) {
                generateErrorCodeResponseExample(operation, apiExceptionCodeExample.value());
            }
            return operation;
        };
    }

    private void generateErrorCodeResponseExample(
            Operation operation, Class<? extends ExceptionCode> type) {
        ApiResponses responses = operation.getResponses();
        // 해당 이넘에 선언된 에러코드들의 목록을 가져옵니다.
        ExceptionCode[] exceptionCodes = type.getEnumConstants();
        // 400, 401, 404 등 에러코드의 상태코드들로 리스트로 모읍니다.
        // 400 같은 상태코드에 여러 에러코드들이 있을 수 있습니다.
        Map<Integer, List<SwaggerExampleHolder>> statusWithExampleHolders =
                Arrays.stream(exceptionCodes)
                        .map(
                                exceptionCode -> {
                                    try {
                                        ExceptionReason exceptionReason = exceptionCode.getExceptionReason();
                                        return SwaggerExampleHolder.builder()
                                                .holder(
                                                        getSwaggerExample(
                                                                exceptionCode.getExplainException(),
                                                                exceptionReason))
                                                .status(exceptionReason.getHttpStatus())
                                                .name(exceptionReason.getErrorCode())
                                                .build();
                                    } catch (NoSuchFieldException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                        .collect(groupingBy(ExampleHolder::getCode));
        // response 객체들을 responses 에 넣습니다.
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private Example getSwaggerExample(String value, ExceptionReason exceptionReason) {
        //ExceptionResponse 는 클라이언트한 실제 응답하는 공통 에러 응답 객체입니다.
        ExceptionResponse exceptionResponse = new ExceptionResponse(exceptionReason,
                "요청시 패스정보입니다.");
        Example example = new Example();
        example.description(value);
        example.setValue(exceptionResponse);
        return example;

    }
}

