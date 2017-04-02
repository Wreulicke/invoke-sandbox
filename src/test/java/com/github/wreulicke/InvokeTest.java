package com.github.wreulicke;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import org.junit.Test;

import lombok.AllArgsConstructor;

public class InvokeTest {
  @Test
  public void test1() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    MethodHandle handle = lookup.bind(new TestObject(), "get", MethodType.methodType(String.class));
    assertThat(handle.invoke()).isEqualTo("getting.");
    MethodHandle handle2 = lookup.findVirtual(TestObject.class, "get", MethodType.methodType(String.class));
    assertThat(handle2.invoke(new TestObject())).isEqualTo("getting.");

  }

  @Test
  public void test2() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    assertThatThrownBy(() -> {
      MethodHandle handle = lookup.bind(new TestObject(), "privateGet", MethodType.methodType(String.class));
      handle.invoke();
    }).isInstanceOf(IllegalAccessException.class);

  }

  @Test
  public void test3() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    MethodHandle handle = lookup.bind(new TestObject(), "packagePrivateGet", MethodType.methodType(String.class));
    assertThat(handle.invoke()).isEqualTo("default getting.");
  }

  @AllArgsConstructor
  public static class TestObject {

    public String get() {
      return "getting.";
    }

    private String privateGet() {
      return "private getting.";
    }

    String packagePrivateGet() {
      return "default getting.";
    }
  }
}
