package com.solidgate.client;

import com.solidgate.auth.SignatureGenerator;
import com.solidgate.config.ApiParameters;
import com.solidgate.model.request.OrderStatusRequest;
import com.solidgate.model.response.OrderStatusResponse;
import com.solidgate.util.JsonBodySerializer;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class OrderStatusClient {

    public OrderStatusResponse getOrderStatus(String orderId) {
        String jsonBody = JsonBodySerializer.toJson(new OrderStatusRequest(orderId));
        String merchantPublicKey = ApiParameters.getMerchantPublicKey();
        String signature = SignatureGenerator.generate(
                merchantPublicKey,
                jsonBody,
                ApiParameters.getSignatureSecretKey()
        );
        return given()
                .header("merchant", merchantPublicKey)
                .header("signature", signature)
                .contentType(JSON)
                .body(jsonBody)
                .post(ApiParameters.getCardPaymentsBaseUrl() + "/status")
                .then()
                .extract()
                .response()
                .as(OrderStatusResponse.class);
    }
}
