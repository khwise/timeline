package com.smilegate.dataproviders.database;

import com.smilegate.core.domain.preferences.BasicDomain;
import com.smilegate.core.usecase.preferences.IBasicPreferencesDataprovider;
import com.smilegate.dataproviders.database.mongodb.entity.TmBasicPreferences;
import com.smilegate.dataproviders.database.mongodb.preferences.write.TmBasicPreferencesWriteRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created by kh.jin on 2020. 4. 28.
 */
@Component
public class BasicPreferencesDataproviderImpl implements IBasicPreferencesDataprovider {

    private final TmBasicPreferencesWriteRepository writeRepository;

    public BasicPreferencesDataproviderImpl(@Qualifier("tmBasicPreferencesWriteRepository") TmBasicPreferencesWriteRepository writeRepository) {
        this.writeRepository = writeRepository;
    }


    @Override
    public BasicDomain saveIntroduce(BasicDomain basicDomain) {
        TmBasicPreferences entity = writeRepository.findByMemberNo(basicDomain.getMemberNo())
                .orElse(TmBasicPreferences.builder()
                        .memberNo(basicDomain.getMemberNo())
                        .introduce(basicDomain.getIntroduce())
                        .regDt(LocalDateTime.now())
                        .updDt(LocalDateTime.now())
                        .build());

        return BasicDomain.builder()
                .memberNo(entity.getMemberNo())
                .introduce(entity.getIntroduce())
                .build();
    }
}
