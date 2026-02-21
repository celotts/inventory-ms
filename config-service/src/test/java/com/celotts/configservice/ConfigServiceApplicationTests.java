package com.celotts.configservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.cloud.config.server.git.uri=https://github.com/spring-cloud-samples/config-repo",
        "spring.cloud.config.server.git.clone-on-start=false"
})
class ConfigServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
