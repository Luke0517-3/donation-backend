package com.irent.donation_backend.config;

import com.irent.donation_backend.api.DonationHandler;
import com.irent.donation_backend.model.Customer;
import com.irent.donation_backend.service.DonationService;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

@Configuration
public class DonationRoutes {

    private static final String MAIN_REQUEST_MAPPING = "/api/donation";
    private static final String GET_CUSTOMER_MAPPING = "/customer/{name}";
    private static final String TEST = "/test";

    @Bean
    public DonationHandler donationHandler(DonationService donationService) {
        return new DonationHandler(donationService);
    }

    @Bean
    public RouterFunction<ServerResponse> donationRouterFunctionSwagger(final DonationHandler handler) {
        return getCustomerFunctionSwagger(handler)
                .and(test(handler));
    }

    protected RouterFunction<ServerResponse> getCustomerFunctionSwagger(final DonationHandler handler) {
        final Consumer<Builder> result = ops -> {
            ops.beanClass(DonationHandler.class)
                    .beanMethod("getCustomer")
                    .operationId("Get Customer Info")
                    .summary("取得捐款目標機構資訊")
                    .description("取得捐款目標機構資訊")
                    .parameter(parameterBuilder()
                            .name("name")
                            .in(ParameterIn.PATH)
                            .required(true)
                            .description("Customer name")
                            .schema(schemaBuilder().type("string"))
                    )
                    .response(responseBuilder()
                            .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE).schema(schemaBuilder().implementation(Customer.class)))
                            .responseCode("200")
                            .description("successful operation")
                    );
            commonProcess(ops);
        };
        return SpringdocRouteBuilder.route().path(MAIN_REQUEST_MAPPING,
                builder -> builder.GET(GET_CUSTOMER_MAPPING, handler::getCustomer), result).build();
    }

    protected RouterFunction<ServerResponse> test(final DonationHandler handler) {
        final Consumer<Builder> result = ops -> {
            ops.beanClass(DonationHandler.class)
                    .beanMethod("test")
                    .operationId("For test")
                    .summary("測試用")
                    .description("測試用")
                    .response(responseBuilder()
                            .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE))
                            .responseCode("200")
                            .description("successful operation")
                    );
            commonProcess(ops);
        };
        return SpringdocRouteBuilder.route().path(MAIN_REQUEST_MAPPING,
                builder -> builder.GET(TEST, handler::test), result).build();
    }

    protected void commonProcess(final Builder builder) {
        builder.response(responseBuilder().responseCode("400").description("Bad Request"))
                .response(responseBuilder().responseCode("401").description("Unauthorized"))
                .response(responseBuilder().responseCode("403").description("Forbidden"))
                .response(responseBuilder().responseCode("404").description("Not found"))
                .response(responseBuilder().responseCode("500").description("Internal Server Error"));
    }
}
