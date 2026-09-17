package com.solidgate.client;

import com.solidgate.auth.SignatureGenerator;
import com.solidgate.config.TestingConfig;
import com.solidgate.model.response.InitPageResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class PaymentPageClient {

    private final String merchantPublicKey;
    private final String signatureSecretKey;
    private final String paymentPageBaseUrl;

    public PaymentPageClient(TestingConfig config) {
        this.merchantPublicKey = config.getMerchantPublicKey();
        this.signatureSecretKey = config.getSignatureSecretKey();
        this.paymentPageBaseUrl = config.getPaymentPageBaseUrl();
    }

    public InitPageResponse createPaymentPage(String jsonBody) {
        String signature = SignatureGenerator.generate(merchantPublicKey, jsonBody, signatureSecretKey);
        return createPaymentPage(jsonBody, merchantPublicKey, signature);
    }

    public InitPageResponse createPaymentPage(String jsonBody, String merchantHeader, String signatureHeader) {
        return given()
                .header("merchant", merchantHeader)
                .header("signature", signatureHeader)
                .contentType(JSON)
                .body(jsonBody)
                .post(paymentPageBaseUrl + "/init")
                .then()
                .extract()
                .response()
                .as(InitPageResponse.class);
    }
}
