package com.github.wreulicke.invoke;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;

public class GetterTest {
  @Test
  public void test1() throws Throwable {
    Lookup publicLookup = MethodHandles.publicLookup();
    assertThatThrownBy(() -> {
      MethodHandle handle = publicLookup.findGetter(TestObject.class, "stringField", String.class);
      handle.invoke(new TestObject("test"));
    }).isInstanceOf(IllegalAccessException.class);
  }

  @Test
  public void test2() throws Throwable {
    Lookup publicLookup = MethodHandles.publicLookup();
    assertThatThrownBy(() -> {
      MethodHandle handle = publicLookup.findGetter(PackagePrivateTestObject.class, "stringField", String.class);
      handle.invoke(new PackagePrivateTestObject("test"));
    }).isInstanceOf(IllegalAccessException.class);
  }

  @Test
  public void test3() throws Throwable {
    Lookup publicLookup = MethodHandles.publicLookup();
    assertThat(publicLookup.findGetter(PublicTestObject.class, "stringField", String.class)
      .invoke(new PublicTestObject("test"))).isEqualTo("test");
  }

  @Test
  public void test4() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    assertThat(lookup.findGetter(TestObject.class, "stringField", String.class)
      .invoke(new TestObject("test"))).isEqualTo("test");
  }

  @Test
  public void test5() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    assertThat(lookup.findGetter(PackagePrivateTestObject.class, "stringField", String.class)
      .invoke(new PackagePrivateTestObject("test"))).isEqualTo("test");
  }

  @Test
  public void test6() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    MethodHandle handle = lookup.findGetter(PublicTestObject.class, "stringField", String.class);
    assertThat(handle.invoke(new PublicTestObject("test"))).isEqualTo("test");
  }

  @AllArgsConstructor
  private static class TestObject {
    public String stringField;
  }

  @AllArgsConstructor
  static class PackagePrivateTestObject {
    public String stringField;
  }

  @AllArgsConstructor
  public static class PublicTestObject {
    public String stringField;
  }
}
