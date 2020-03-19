package com.github.wreulicke.reflect.doc3;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

interface MyInterface{
  String get();
}

public class ProxyTest {

  @Test
  public void test() {
    final MyInterface instance = (MyInterface) Proxy.newProxyInstance(ProxyTest.class.getClassLoader(), new Class[]{MyInterface.class},
        (proxy, method, args) -> "invoked: " + method.getName());
    assertThat(instance.get()).isEqualTo("invoked: get");
  }
}


