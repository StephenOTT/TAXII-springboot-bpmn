package io.digitalstate.taxii.common;

import org.springframework.http.HttpHeaders;

public class Headers {

    public static HttpHeaders getSuccessHeaders(){
        HttpHeaders successHeaders = new HttpHeaders();
        successHeaders.add("content-type", "application/vnd.oasis.taxii+json");
        successHeaders.add("verison", "2.0");
        return successHeaders;
    }
}
