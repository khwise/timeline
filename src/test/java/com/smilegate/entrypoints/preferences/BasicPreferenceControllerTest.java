package com.smilegate.entrypoints.preferences;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Created by kh.jin on 2020. 4. 28.
 */
@ExtendWith(SpringExtension.class)
@DisplayName("기본설정 Controller 테스트")
class BasicPreferenceControllerTest {
    static WireMockServer wireMockServer;

    @BeforeAll
    static void before() {
        wireMockServer = new WireMockServer(9091);
        wireMockServer.start();
    }


    @AfterAll
    static void shutdown() {
        wireMockServer.shutdown();
    }
}