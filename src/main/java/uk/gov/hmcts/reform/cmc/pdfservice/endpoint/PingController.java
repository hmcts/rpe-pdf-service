package uk.gov.hmcts.reform.cmc.pdfservice.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class PingController {

    @GetMapping("/ping")
    @ApiOperation("Get Ping")
    public String ping() {
        return "OK";
    }
}
