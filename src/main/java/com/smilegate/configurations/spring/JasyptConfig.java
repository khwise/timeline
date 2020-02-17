package com.smilegate.configurations.spring;

import org.jasypt.encryption.ByteEncryptor;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEByteEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.salt.StringFixedSaltGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * https://github.com/ulisesbocchio/jasypt-spring-boot
 */
@Configuration
public class JasyptConfig {

    @Bean("jasyptStringEncryptor")
    public static StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(getConfig());
        return encryptor;
    }

    @Bean("jasyptByteEncryptor")
    public static ByteEncryptor byteEncryptor() {
        PooledPBEByteEncryptor encryptor = new PooledPBEByteEncryptor();
        encryptor.setConfig(getConfig());
        return encryptor;
    }

    public static SimpleStringPBEConfig getConfig(){
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("vjPC7Ifxej5OonY394uWQw"); //암호화에 사용할 키
        //see provider information at https://docs.oracle.com/javase/8/docs/technotes/guides/security/SunProviders.html#SunJCEProvider
        //config.setProvider(new BouncyCastleProvider());
        config.setProviderName("SunJCE");
        //config.setAlgorithm("PBEWithHmacSHA256AndAES_128");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPoolSize( Runtime.getRuntime().availableProcessors() );  // Improving performance in multi-core
        config.setStringOutputType("base64");
        //config.setKeyObtentionIterations("1000");
        //config.setSaltGenerator(new PropertyEncryptionSalt());
        return config;
    }

    public static class PropertyEncryptionSalt extends StringFixedSaltGenerator {

        public PropertyEncryptionSalt() {
            super("vjPC7Ifxej5OonY394uWQw","UTF-8");
        }
    }


}
