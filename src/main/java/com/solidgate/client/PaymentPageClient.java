package com.solidgate.client;

import com.solidgate.auth.SignatureGenerator;
import com.solidgate.config.ApiParameters;
import com.solidgate.model.response.InitPageResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class PaymentPageClient {

    public InitPageResponse createPaymentPage(String jsonBody) {
        String merchantPublicKey = ApiParameters.getMerchantPublicKey();
        String signature = SignatureGenerator.generate(
                merchantPublicKey,
                jsonBody,
                ApiParameters.getSignatureSecretKey()
        );
        return createPaymentPage(jsonBody, merchantPublicKey, signature);
    }

    public InitPageResponse createPaymentPage(String jsonBody, String merchantHeader, String signatureHeader) {
        return given()
                .header("merchant", merchantHeader)
                .header("signature", signatureHeader)
                .contentType(JSON)
                .body(jsonBody)
                .post(ApiParameters.getPaymentPageBaseUrl() + "/init")
                .then()
                .extract()
                .response()
                .as(InitPageResponse.class);
    }
}
