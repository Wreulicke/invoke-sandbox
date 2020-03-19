package com.github.wreulicke.jackson;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.Value;

class JacksonTest {

  @Test
  public void test1() throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    Hoge hoge = mapper.readValue("{ \"str\" : \"test\" }", Hoge.class);
    System.out.println(hoge);
    Fuga fuga = mapper.readValue("{ \"str\" : \"test\" }", Fuga.class);
    System.out.println(fuga);
    Moga moga = mapper.readValue("{ \"str\" : \"test\" }", Moga.class);
    System.out.println(moga);
  }

  @Data
  public static class Fuga {
    private String str;
  }

  @Value
  public static class Hoge {
    private String str;
  }

  @Value
  public static class Moga {
    private String str;
    private String yyy;
  }
}
