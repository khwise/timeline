package com.smilegate.core.usecase.preferences;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smilegate.core.domain.preferences.BasicDomain;
import com.smilegate.core.usecase.IUsecase;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by kh.jin on 2020. 4. 28.
 */
@Component
public class TmIntroducePreferenceUsecase implements IUsecase<TmIntroducePreferenceUsecase.Command, TmIntroducePreferenceUsecase.Result> {
    private final static Logger log = LoggerFactory.getLogger(TmIntroducePreferenceUsecase.class);

    @Autowired
    private IBasicPreferencesDataprovider dataprovider;

    @Override
    public TmIntroducePreferenceUsecase.Result execute(TmIntroducePreferenceUsecase.Command command) {
        BasicDomain basicDomain = new BasicDomain();
        basicDomain.setMemberNo(command.memberNo);
        basicDomain.setIntroduce(command.introduce);
        dataprovider.saveIntroduce(basicDomain);

        Result result = new Result();
        result.setMemberNo(basicDomain.getMemberNo());
        result.setIntroduce(basicDomain.getIntroduce());
        return result;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Command implements Serializable, IUsecase.ICommand {
        private Long memberNo;
        private String introduce;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Result implements Serializable, IUsecase.IResult {
        private Long memberNo;
        private String introduce;
    }

}
