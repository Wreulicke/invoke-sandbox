package com.github.wreulicke.reflect;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Proxy;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ClassTest {

  public static class EnclosedType {
    private class Inner {
    }
  }

  @Nested
  class 基本的なクラスのテスト {

    @Test
    void testGetName() {
      assertThat(ClassTest.class.getName()).isEqualTo("com.github.wreulicke.reflect.ClassTest");
    }

    @Test
    void testGetCanonicalName() {
      assertThat(ClassTest.class.getCanonicalName()).isEqualTo("com.github.wreulicke.reflect.ClassTest");
    }

    @Test
    void testGetSimpleName() {
      assertThat(ClassTest.class.getSimpleName()).isEqualTo("ClassTest");
    }

    @Test
    void testGetTypeName() {
      assertThat(ClassTest.class.getTypeName()).isEqualTo("com.github.wreulicke.reflect.ClassTest");
    }

    @Test
    void testGetTypeParameter() {
      assertThat(ClassTest.class.getTypeParameters()).isEmpty();
    }

    @Test
    void testGetClassInstance() {
      ClassTest test = new ClassTest();
      assertThat(test.getClass()).isEqualTo(ClassTest.class);
      assertThat(test.getClass()
        .getName()).isEqualTo("com.github.wreulicke.reflect.ClassTest");
    }

    @Test
    void testAssignable() {
      assertThat(ClassTest.class.isAssignableFrom(new ClassTest() {}.getClass())).isTrue();
    }

    @Test
    void testAssignableLocalClass() {
      class LocalClass extends ClassTest {
      }
      assertThat(ClassTest.class.isAssignableFrom(LocalClass.class)).isTrue();
    }


    @Test
    void testAssignableOther() {
      assertThat(ClassTest.class.isAssignableFrom(new Object() {}.getClass())).isFalse();
    }

  }

  class Enclosedなクラス {

    @Test
    void testGetName() {
      assertThat(EnclosedType.class.getName()).isEqualTo("com.github.wreulicke.reflect.ClassTest$EnclosedType");
    }

    @Test
    void testGetSimpleName() {
      assertThat(EnclosedType.class.getSimpleName()).isEqualTo("EnclosedType");
    }

    @Test
    void testGetEnclosingClass() {
      assertThat(EnclosedType.class.getEnclosingClass()).isEqualTo(ClassTest.class);
    }

    @Test
    void testGetCanonicalName() {
      assertThat(EnclosedType.class.getCanonicalName()).isEqualTo("com.github.wreulicke.reflect.ClassTest.EnclosedType");
    }

  }


  @Nested
  class 配列の周辺 {
    Class<?> clazz1 = ClassTest.class;
    Class<?> clazz2 = ClassTest[].class;

    @Test
    void testGetComponentType() {
      assertThat(clazz2.getComponentType()).isEqualTo(clazz1);
    }

    @Test
    void testIsArray1() {
      assertThat(clazz1.isArray()).isFalse();
    }

    @Test
    void testIsArray2() {
      assertThat(clazz2.isArray()).isTrue();
    }

    public <T> Class<?> getComponentType(@SuppressWarnings("unchecked") T... arrays) {
      return arrays.getClass()
        .getComponentType();
    }

    @Test
    void testVariadicMethod() {
      assertThat(getComponentType(new ClassTest())).isEqualTo(ClassTest.class);
    }
  }

  @Nested
  class 合成って何 {
    @Test
    void testSimple() {
      assertThat(ClassTest.class.isSynthetic()).isFalse();
    }

    @Test
    void testEnclosed() {
      assertThat(EnclosedType.class.isSynthetic()).isFalse();
    }

    @Test
    void testAnonymousClass() {
      assertThat(new Object() {}.getClass()
        .isSynthetic()).isFalse();
    }

    @Test
    void testInnerClass() {
      assertThat(EnclosedType.Inner.class.isSynthetic()).isFalse();
      assertThat(new EnclosedType().new Inner().getClass()
        .isSynthetic()).isFalse();
    }

    @Test
    void testArrayType() {
      assertThat(ClassTest[].class.isSynthetic()).isFalse();
    }

    @Test
    void testProxy() {
      assertThat(Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[] {
        Predicate.class
      }, (proxy, name, args) -> null)
        .getClass()
        .isSynthetic()).isFalse();
    }

    @Test
    void testLambda() {
      assertThat(((Predicate<Integer>) (Integer i) -> true).getClass()
        .isSynthetic()).isTrue();
    }

    @Test
    void testMethodHandle() throws NoSuchFieldException, IllegalAccessException {
      Lookup lookup = MethodHandles.publicLookup();
      MethodHandle handle = lookup.findStaticGetter(System.class, "out", PrintStream.class);
      assertThat(MethodHandleProxies.asInterfaceInstance(Supplier.class, handle)
        .getClass()
        .isSynthetic()).isFalse();
    }
  }


}
