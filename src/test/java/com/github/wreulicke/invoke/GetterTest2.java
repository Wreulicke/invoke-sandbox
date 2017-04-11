package com.github.wreulicke.invoke;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

import org.junit.Test;

import lombok.AllArgsConstructor;

public class GetterTest2 {

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
    assertThatThrownBy(() -> {
      MethodHandle handle = publicLookup.findGetter(PublicTestObject.class, "stringField", String.class);
      handle.invoke(new PublicTestObject("test"));
    }).isInstanceOf(IllegalAccessException.class);
  }

  @Test
  public void test4() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    assertThatThrownBy(() -> {
      MethodHandle handle = lookup.findGetter(TestObject.class, "stringField", String.class);
      handle.invoke(new TestObject("test"));
    }).isInstanceOf(IllegalAccessException.class);
  }

  @Test
  public void test5() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    assertThatThrownBy(() -> {
      MethodHandle handle = lookup.findGetter(PackagePrivateTestObject.class, "stringField", String.class);
      handle.invoke(new PackagePrivateTestObject("test"));
    }).isInstanceOf(IllegalAccessException.class);
  }

  @Test
  public void test6() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    assertThatThrownBy(() -> {
      MethodHandle handle = lookup.findGetter(PublicTestObject.class, "stringField", String.class);
      handle.invoke(new PublicTestObject("test"));
    }).isInstanceOf(IllegalAccessException.class);
  }

  @AllArgsConstructor
  private static class TestObject {
    private String stringField;
  }

  @AllArgsConstructor
  static class PackagePrivateTestObject {
    private String stringField;
  }

  @AllArgsConstructor
  public static class PublicTestObject {
    private String stringField;
  }
}
