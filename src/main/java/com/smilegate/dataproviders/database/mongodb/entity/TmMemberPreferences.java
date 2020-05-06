package com.smilegate.dataproviders.database.mongodb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 회원 타임라인 설정 관련 Entity위
 *
 * Column 설명
 * id : ObjectID
 * memberNo : 회원 번호 @Indexed( name = "app_memberNo_1", direction = IndexDirection.ASCENDING , background = true)
 * covrage : 게시글 공개 범위
 * tmCoverageType : 타임라인 공개 범위
 *
 * @Date 2020.04.01
 * @author yiseungju
 * @version 1.0
 */
@Data
@Document("tmMemberPreferences")
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TmMemberPreferences implements Serializable {
    private static final long serialVersionUID = 8687787277716275867L;

    @Id
    private ObjectId id;

    private Long memberNo;

    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime systemDt;

    @CreatedDate
    private LocalDateTime regDt;

    @LastModifiedDate
    private LocalDateTime updDt;
}
