package com.smilegate.entrypoints.preferences;

import com.smilegate.annotations.Authorization;
import com.smilegate.annotations.AuthorizationPermit;
import com.smilegate.core.domain.member.MemberDomain;
import com.smilegate.core.usecase.preferences.TmIntroducePreferenceUsecase;
import com.smilegate.entrypoints.RestApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Member 별 공개 범위 설정 Entrypoint
 *
 * Community 글 공개범위 설정 기능 ( PRIVATE: 비공개, PUBLIC: 전체공개, FRIEND: 친구공개 )
 * Timeline 글 공개범위 설정 기능 ( PRIVATE: 비공개, PUBLIC: 전체공개, FRIEND: 친구공개 )
 *
 * @Date 2020.04.01
 * @author yiseungju
 * @version 1.0
 */
@RestController
@RequestMapping("/tm/p/v1/preferences/basic")
@RequiredArgsConstructor
public class BasicPreferenceController {
    Logger logger = LogManager.getLogger(BasicPreferenceController.class);

    private final TmIntroducePreferenceUsecase tmIntroducePreferenceUsecase;

    /**
     * 커뮤니티 공개범위 설정 등록
     * /tm/p/v1/preferences/basic/introduce/post
     * wiki API doc :
     *
     * @param command
     * @param member
     * @return
     */
    @RequestMapping(value = "/introduce/post", method = RequestMethod.POST)
    public RestApiResponse<TmIntroducePreferenceUsecase.Result> introduce(@RequestBody @Valid TmIntroducePreferenceUsecase.Command command,
                                                                         @Authorization(permit = {AuthorizationPermit.USER}) MemberDomain member) {
        command.setMemberNo(member.getMemberNo());
        TmIntroducePreferenceUsecase.Result result = tmIntroducePreferenceUsecase.execute(command);

        return RestApiResponse.ok(result);
    }
}
