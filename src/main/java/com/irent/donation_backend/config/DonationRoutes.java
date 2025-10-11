package com.irent.donation_backend.config;

import com.irent.donation_backend.api.DonationHandler;
import com.irent.donation_backend.model.lark.Customer;
import com.irent.donation_backend.model.lark.NGOOrderFields;
import com.irent.donation_backend.model.newebpay.NewebPayNotifyReqDTO;
import com.irent.donation_backend.model.newebpay.NewebPayReqDTO;
import com.irent.donation_backend.model.newebpay.OrderInfoDTO;
import com.irent.donation_backend.service.DonationService;
import com.irent.donation_backend.service.NewebPayService;
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
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

@Configuration
public class DonationRoutes {

    /**
     * API 路徑常數
     */
    public static class ApiPaths {
        public static final String BASE = "/api/donation";
        public static final String CUSTOMER = "/customer/{name}";
        public static final String ORDER = "/create/order";
        public static final String NEWEBPAY = "/newebpay/retrieve";
        public static final String TEST = "/test";
        public static final String NEWEBPAY_NOTIFY = "/newebpay/notify";
    }

    /**
     * 註冊 DonationHandler Bean
     */
    @Bean
    public DonationHandler donationHandler(DonationService donationService, NewebPayService newebPayService) {
        return new DonationHandler(donationService, newebPayService);
    }

    /**
     * 註冊所有捐款相關路由
     */
    @Bean
    public RouterFunction<ServerResponse> donationRouterFunction(final DonationHandler handler) {
        return customerRoute(handler)
                .and(orderRoute(handler))
                .and(newebPayRoute(handler))
                .and(newebPayNotifyRoute(handler));
        // 如需啟用測試路由，請取消下面註解
        // .and(testRoute(handler));
    }

    /**
     * 取得捐款目標機構資訊路由
     */
    protected RouterFunction<ServerResponse> customerRoute(final DonationHandler handler) {
        return SpringdocRouteBuilder.route().path(ApiPaths.BASE,
                builder -> builder.GET(ApiPaths.CUSTOMER, handler::getCustomer),
                createSwaggerDocs("getCustomer", "Get Customer Info",
                        "取得捐款目標機構資訊", "取得捐款目標機構資訊",
                        null, Customer.class)
                        .andThen(ops -> ops.parameter(parameterBuilder()
                                .name("name")
                                .in(ParameterIn.PATH)
                                .required(true)
                                .description("客戶名稱")
                                .schema(schemaBuilder().type("string"))))
        ).build();
    }

    /**
     * 建立捐款訂單並取得藍星支付請求資訊路由
     */
    protected RouterFunction<ServerResponse> orderRoute(final DonationHandler handler) {
        return SpringdocRouteBuilder.route().path(ApiPaths.BASE,
                builder -> builder.POST(ApiPaths.ORDER, handler::createOrder),
                createSwaggerDocs("createOrder", "Create Order For Donation and Retrieve NewebPay Request",
                        "建立捐款訂單並取得藍星支付請求資訊", "建立捐款訂單並取得藍星支付請求資訊",
                        NGOOrderFields.class, NewebPayReqDTO.class)
        ).build();
    }

    /**
     * 藍星支付請求路由
     */
    protected RouterFunction<ServerResponse> newebPayRoute(final DonationHandler handler) {
        return SpringdocRouteBuilder.route().path(ApiPaths.BASE,
                builder -> builder.POST(ApiPaths.NEWEBPAY, handler::retrieveNewebPayRequest),
                createSwaggerDocs("retrieveNewebPayRequest", "Retrieve NewebPay Request",
                        "取得藍星支付請求資訊", "取得藍星支付請求資訊",
                        OrderInfoDTO.class, NewebPayReqDTO.class)
        ).build();
    }

    /**
     * 藍新支付通知路由
     */
    protected RouterFunction<ServerResponse> newebPayNotifyRoute(final DonationHandler handler) {
        return SpringdocRouteBuilder.route().path(ApiPaths.BASE,
                builder -> builder.POST(ApiPaths.NEWEBPAY_NOTIFY, handler::handleNewebPayNotify),
                createSwaggerDocs("handleNewebPayNotify", "NewebPay Notification Handler",
                        "處理藍新支付通知", "接收並處理藍新支付結果通知",
                        NewebPayNotifyReqDTO.class, String.class)
        ).build();
    }

    /**
     * 測試用路由
     */
    protected RouterFunction<ServerResponse> testRoute(final DonationHandler handler) {
        return SpringdocRouteBuilder.route().path(ApiPaths.BASE,
                builder -> builder.POST(ApiPaths.TEST, handler::test),
                createSwaggerDocs("test", "For test",
                        "測試用", "測試用",
                        NGOOrderFields.class, String.class)
        ).build();
    }

    /**
     * 建立 Swagger 文檔設定
     *
     * @param methodName   處理方法名稱
     * @param operationId  操作 ID
     * @param summary      摘要
     * @param description  描述
     * @param requestType  請求體類型，如不需要可傳 null
     * @param responseType 回應類型
     * @return Swagger 配置
     */
    private <T, R> Consumer<Builder> createSwaggerDocs(
            String methodName, String operationId, String summary, String description,
            Class<T> requestType, Class<R> responseType) {

        return ops -> {
            ops.beanClass(DonationHandler.class)
                    .beanMethod(methodName)
                    .operationId(operationId)
                    .summary(summary)
                    .description(description);

            if (requestType != null) {
                ops.requestBody(requestBodyBuilder()
                        .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(requestType)))
                        .required(true));
            }

            ops.response(responseBuilder()
                    .content(contentBuilder().mediaType(MediaType.APPLICATION_JSON_VALUE)
                            .schema(schemaBuilder().implementation(responseType)))
                    .responseCode("200")
                    .description("successful operation"));

            commonProcess(ops);
        };
    }

    /**
     * 添加通用回應代碼
     */
    protected void commonProcess(final Builder builder) {
        builder.response(responseBuilder().responseCode("400").description("Bad Request"))
                .response(responseBuilder().responseCode("401").description("Unauthorized"))
                .response(responseBuilder().responseCode("403").description("Forbidden"))
                .response(responseBuilder().responseCode("404").description("Not found"))
                .response(responseBuilder().responseCode("500").description("Internal Server Error"));
    }
}