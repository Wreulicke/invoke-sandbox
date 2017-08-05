package com.github.wreulicke.invoke;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.text.MessageFormat;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;

public class InvokeTest2 {

  @Test
  public void test0() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    MethodHandle handle = lookup.findVirtual(HasMethodObject.class, "getField", MethodType.methodType(String.class, String.class));
    assertThat(handle.invoke(new HasMethodObject("yyy"), "xxxx")).isEqualTo("yyyxxxx");
  }

  @Test
  public void test1() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    MethodHandle handle = lookup.findVirtual(HasMethodObject.class, "getField", MethodType.methodType(String.class, String.class));
    MethodHandle handle2 = MethodHandles.insertArguments(handle, 1, "xxxx");
    assertThat(handle2.invoke(new HasMethodObject("yyy"))).isEqualTo("yyyxxxx");

    MethodHandle permuted = MethodHandles.permuteArguments(handle, MethodType.methodType(String.class, String.class, HasMethodObject.class), 1, 0);;
    assertThat(permuted.invoke("zzzz", new HasMethodObject("yyy"))).isEqualTo("yyyzzzz");

  }

  @Test
  public void test2() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    BiFunction<Integer, Integer, Integer> addFunction = (x, y) -> x + y;
    MethodHandle lambda = lookup.findVirtual(BiFunction.class, "apply", MethodType.methodType(Object.class, Object.class, Object.class));
    assertThat(lambda.invoke(addFunction, 1, 2)).isEqualTo(3);

    assertThat(MethodHandles.dropArguments(lambda.bindTo(addFunction), 1, Object.class)
      .invoke(1, 2, 3)).isEqualTo(4);
    assertThat(lambda.bindTo(addFunction)
      .bindTo(2)
      .invoke(1)).isEqualTo(3);
  }

  @Test
  public void test3() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    MethodType methodType = MethodType.genericMethodType(1, true)
      .changeReturnType(String.class)
      .changeParameterType(0, String.class);
    assertThat(methodType.toMethodDescriptorString()).isEqualTo("(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;");
    MethodHandle handle = lookup.findStatic(MessageFormat.class, "format", methodType);
    MethodHandle formatter = handle.bindTo("format strings {0} {1}");
    assertThat(formatter.asVarargsCollector(Object[].class)
      .invoke("1", "2")).isEqualTo("format strings 1 2");
    assertThat(formatter.invoke(new Object[] {
      "1",
      "2"
    })).isEqualTo("format strings 1 2");
  }

  @Test
  public void test4() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    MethodType methodType = MethodType.genericMethodType(1, true)
      .changeReturnType(String.class)
      .changeParameterType(0, String.class);

    MethodHandle formatter = lookup.findStatic(MessageFormat.class, "format", methodType)
      .bindTo("format strings {0} {1}");
    MethodHandle trace = lookup.findVirtual(PrintStream.class, "printf", MethodType.methodType(PrintStream.class, new Class[] {
      String.class,
      Object[].class
    }))
      .bindTo(System.out)
      .asType(MethodType.methodType(void.class, String.class, Object[].class))
      .bindTo("arguments %s %s");

    // also prints "arguments xxx yyy"
    assertThat(MethodHandles.foldArguments(formatter, trace)
      .invoke(new Object[] {
        "xxx",
        "yyy"
    })).isEqualTo("format strings xxx yyy");
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

  @AllArgsConstructor
  public static class HasMethodObject {
    private final String string;

    public String getField() {
      return string;
    }

    public String getField(String xxxx) {
      return string + xxxx;
    }
  }
}
