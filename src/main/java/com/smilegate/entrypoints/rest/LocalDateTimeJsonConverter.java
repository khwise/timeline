package com.smilegate.entrypoints.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Json 응답의 serialize/deserialize 전역 설정.
 * 모든 LocalDateTime에 대해서 long 형으로 변환하고 long 데이터는 LocalDateTime 으로 받을 때 변환처리 함
 */
@JsonComponent
public class LocalDateTimeJsonConverter {
    public static class LocalDateTimeJsonSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            Date out = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            jsonGenerator.writeNumber(out.getTime());
        }
    }
    public static class LocalDateTimeJsonDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            Date in = new Date(jsonParser.getValueAsLong());
            return LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        }
    }
}
