package com.smilegate.entrypoints.command.preferences;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DisplayName("공개범위 설정 Controller 테스트")
class CoveragePreferenceControllerTest {
    static WireMockServer wireMockServer;

    @BeforeAll
    static void before(){
        wireMockServer = new WireMockServer(9091);
        wireMockServer.start();
    }

    @Test
    void timeLineCovrage() {
    }

    @Test
    void communityCoverage() {
    }

    @AfterAll
    static void shutdown(){
        wireMockServer.stop();
    }
}