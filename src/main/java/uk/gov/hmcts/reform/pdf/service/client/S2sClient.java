package uk.gov.hmcts.reform.pdf.service.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(name = "s2s", url = "${s2s.url}")
public interface S2sClient {

    @RequestMapping(method = GET, path = "/details")
    String getServiceName(@RequestHeader(AUTHORIZATION) String authHeader);
}
