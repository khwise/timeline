package com.smilegate.dataproviders.database.mongodb.write;

import com.smilegate.configurations.spring.MongoDBMasterConfig;
import com.smilegate.dataproviders.database.mongodb.Card;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ContextConfiguration(classes={
        MongoDBMasterConfig.class
},
        loader = AnnotationConfigContextLoader.class
)
@EnableEncryptableProperties
@TestPropertySource(locations ="classpath:application_test.properties")
@SpringBootTest
public class CardWriteRepositoryTest {

    @Autowired
    @Qualifier("cardWriteRepository")
    private CardWriteRepository cardWriteRepository;

    @Test
    public void saveCardTest(){

        Card card = new Card();
        card.setCardNo(String.valueOf(System.currentTimeMillis()));

        Card saved = cardWriteRepository.save(card);

        System.out.println(saved.getId());

    }
}
