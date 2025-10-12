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

        // 按業務功能分類的路徑
        public static class Customer {
            public static final String GET_INFO = "/customer/{name}";
        }

        public static class Order {
            public static final String CREATE = "/order/create";
            public static final String QUERY = "/order/{orderId}";
        }

        public static class NewebPay {
            public static final String RETRIEVE = "/newebpay/retrieve";
            public static final String NOTIFY = "/newebpay/notify";
            public static final String REDIRECT = "/newebpay/redirect";
        }
    }

    /**
     * 註冊 DonationHandler Bean
     */
    @Bean
    public DonationHandler donationHandler(DonationService donationService, NewebPayService newebPayService, NewebPayProperties newebPayProperties) {
        return new DonationHandler(donationService, newebPayService, newebPayProperties);
    }

    /**
     * 註冊所有捐款相關路由
     */
    @Bean
    public RouterFunction<ServerResponse> donationRouterFunction(final DonationHandler handler) {
        return customerRoutes(handler)
                .and(orderRoutes(handler))
                .and(newebPayRoutes(handler));
    }

    /**
     * 客戶相關路由
     */
    protected RouterFunction<ServerResponse> customerRoutes(final DonationHandler handler) {
        return SpringdocRouteBuilder.route().path(ApiPaths.BASE,
                builder -> builder.GET(ApiPaths.Customer.GET_INFO, handler::getCustomer),
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
     * 訂單相關路由
     */
    protected RouterFunction<ServerResponse> orderRoutes(final DonationHandler handler) {
        // 創建訂單路由
        RouterFunction<ServerResponse> createOrderRoute = SpringdocRouteBuilder.route().path(ApiPaths.BASE,
                builder -> builder.POST(ApiPaths.Order.CREATE, handler::createOrder),
                createSwaggerDocs("createOrder", "Create Order For Donation",
                        "建立捐款訂單並取得藍星支付請求資訊", "建立捐款訂單並取得藍星支付請求資訊",
                        NGOOrderFields.class, NewebPayReqDTO.class)
        ).build();

        // 查詢訂單路由
        RouterFunction<ServerResponse> queryOrderRoute = SpringdocRouteBuilder.route().path(ApiPaths.BASE,
                builder -> builder.GET(ApiPaths.Order.QUERY, handler::queryOrderInfo),
                createSwaggerDocs("queryOrderInfo", "Query Order Info",
                        "查詢訂單資訊", "查詢訂單資訊",
                        null, Object.class)
                        .andThen(ops -> ops.parameter(parameterBuilder()
                                .name("orderId")
                                .in(ParameterIn.PATH)
                                .required(true)
                                .description("訂單編號")
                                .schema(schemaBuilder().type("string"))))
        ).build();

        // 合併並返回所有訂單相關路由
        return createOrderRoute.and(queryOrderRoute);
    }

    /**
     * 藍星支付相關路由
     */
    protected RouterFunction<ServerResponse> newebPayRoutes(final DonationHandler handler) {
        // 取得支付請求路由
        RouterFunction<ServerResponse> retrieveRoute = SpringdocRouteBuilder.route().path(ApiPaths.BASE,
                builder -> builder.POST(ApiPaths.NewebPay.RETRIEVE, handler::retrieveNewebPayRequest),
                createSwaggerDocs("retrieveNewebPayRequest", "Retrieve NewebPay Request",
                        "取得藍星支付請求資訊", "取得藍星支付請求資訊",
                        OrderInfoDTO.class, NewebPayReqDTO.class)
        ).build();

        // 處理支付通知路由
        RouterFunction<ServerResponse> notifyRoute = SpringdocRouteBuilder.route().path(ApiPaths.BASE,
                builder -> builder.POST(ApiPaths.NewebPay.NOTIFY, handler::handleNewebPayNotify),
                createSwaggerDocs("handleNewebPayNotify", "NewebPay Notification Handler",
                        "處理藍新支付通知", "接收並處理藍新支付結果通知",
                        NewebPayNotifyReqDTO.class, String.class)
        ).build();

        // 處理支付返回路由
        RouterFunction<ServerResponse> redirectRoute = SpringdocRouteBuilder.route().path(ApiPaths.BASE,
                builder -> builder.POST(ApiPaths.NewebPay.REDIRECT, handler::handleNewebPayReturn),
                createSwaggerDocs("handleNewebPayReturn", "NewebPay Return Handler",
                        "處理藍星支付返回並重導向", "接收藍星支付返回結果並根據狀態重導向到成功或失敗頁面",
                        NewebPayNotifyReqDTO.class, Void.class)
        ).build();

        // 合併並返回所有藍星支付相關路由
        return retrieveRoute.and(notifyRoute).and(redirectRoute);
    }

    /**
     * 建立 Swagger 文檔設定
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