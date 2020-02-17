package com.smilegate.configurations;


import com.smilegate.configurations.spring.JasyptConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.*;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes={
        JasyptConfig.class
},
        loader = AnnotationConfigContextLoader.class
)
@EnableEncryptableProperties
@TestPropertySource(locations ="classpath:application_test.properties")
@SpringBootTest
public class JasyptConfigTest {

    @Autowired
    @Qualifier("jasyptStringEncryptor")
    private StringEncryptor jasyptStringEncryptor;

    @Value("${config.mongos.password}")
    private String plainPassword;

    @Test
    public void whenConfiguredExcryptorUsed_ReturnCustomEncryptor() {
        String plain = "appqa";
        String enc = jasyptStringEncryptor.encrypt(plain);
        System.out.println("plain txt:"+plain+", encrypted text:"+enc);
        String dec = jasyptStringEncryptor.decrypt(enc);
        assertEquals( plain , dec );
        System.out.println(plainPassword);

        assertEquals( "appqa", jasyptStringEncryptor.decrypt("lMnsYn3oOsgZ1tgWBW/CTA=="));

        assertEquals("appqa", plainPassword);
    }
}
