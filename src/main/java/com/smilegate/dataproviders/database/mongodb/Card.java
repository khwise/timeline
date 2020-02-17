package com.smilegate.dataproviders.database.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document(collection = "card")
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@CompoundIndexes({
    @CompoundIndex(
        name = "app_cardNo_1",
        def = "{'cardNo': 1}",
        background  = true
    )
})
public class Card implements Serializable {
    private static final long serialVersionUID = 6512332286534302515L;

    @Id
    @JsonIgnore
    private ObjectId id;

    @LastModifiedDate
    private LocalDateTime systemDt;

    protected String cardNo;
}
