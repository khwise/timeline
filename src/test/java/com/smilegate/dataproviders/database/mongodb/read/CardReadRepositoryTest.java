package com.smilegate.dataproviders.database.mongodb.read;

import com.smilegate.configurations.spring.MongoDBMasterConfig;
import com.smilegate.configurations.spring.MongoDBSlaveConfig;
import com.smilegate.dataproviders.database.mongodb.Card;
import com.smilegate.dataproviders.database.mongodb.QCard;
import com.smilegate.dataproviders.database.mongodb.write.CardWriteRepository;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes={
        MongoDBMasterConfig.class,
        MongoDBSlaveConfig.class
},
        loader = AnnotationConfigContextLoader.class
)
@EnableEncryptableProperties
@TestPropertySource(locations ="classpath:application_test.properties")
@SpringBootTest
public class CardReadRepositoryTest {

    @Autowired
    @Qualifier("cardWriteRepository")
    private CardWriteRepository cardWriteRepository;

    @Autowired
    @Qualifier("cardReadRepository")
    private CardReadRepository cardReadRepository;

    @Test
    public void findCardTest(){

        Card card = new Card();
        card.setCardNo(String.valueOf(System.currentTimeMillis()));

        Card saved = cardWriteRepository.save(card);

        Optional<Card> findedCard = cardReadRepository.findById(saved.getId());

        assertEquals("동일 카드?" , saved.getCardNo(), findedCard.orElse(null).getCardNo() );

        System.out.println(saved.getId());

    }

    @Test
    public void findWithQueryDsl(){
        // 저장
        Card card = new Card();
        card.setCardNo(String.valueOf(System.currentTimeMillis()));
        Card saved = cardWriteRepository.save(card);

        // querydsl 로 find
        Optional<Card> find = cardReadRepository.findOne(QCard.card.cardNo.eq(saved.getCardNo() ));

        assertEquals(saved.getId(), find.orElse(new Card()).getId());

    }
}
