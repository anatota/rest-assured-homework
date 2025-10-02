package ge.tbc.testautomation.api.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

public abstract class BaseApi {

    public static final RequestSpecification REQ_SPEC = new RequestSpecBuilder()
            .setContentType("application/json")
            .log(LogDetail.ALL)
            .build()
            .filters(new ResponseLoggingFilter());
}
