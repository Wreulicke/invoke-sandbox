package com.github.wreulicke.invoke;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

class ProxiesTest {

  @SuppressWarnings("unused")
  private static boolean test(Object x) {
    return x instanceof String;
  }


  @Test
  void test1() throws Throwable {
    Lookup lookup = MethodHandles.lookup();
    MethodHandle handle = lookup.findStatic(ProxiesTest.class, "test", MethodType.methodType(boolean.class, Object.class));

    @SuppressWarnings("unchecked")
    Predicate<Object> predicate = MethodHandleProxies.asInterfaceInstance(Predicate.class, handle);
    assertThat(predicate.test("test")).isEqualTo(true);
    assertThat(predicate.test(1)).isEqualTo(false);
    assertThatThrownBy(predicate::negate).isInstanceOf(InternalError.class);
  }
}
