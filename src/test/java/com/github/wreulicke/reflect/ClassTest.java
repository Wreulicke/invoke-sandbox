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

import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;

@XmlRootElement
public class ClassTest {
  public static class EnclosedType {
    private class Inner {
    }
  }

  /**
   * クラス編
   */
  @Test
  public void test1() {
    assertThat(ClassTest.class.getName()).isEqualTo("com.github.wreulicke.reflect.ClassTest");
    assertThat(new ClassTest().getClass()
      .getName()).isEqualTo("com.github.wreulicke.reflect.ClassTest");

    assertThat(EnclosedType.class.getName()).isEqualTo("com.github.wreulicke.reflect.ClassTest$EnclosedType");
    assertThat(EnclosedType.class.getEnclosingClass()).isEqualTo(ClassTest.class);

    assertThat(ClassTest.class.getCanonicalName()).isEqualTo("com.github.wreulicke.reflect.ClassTest");
    assertThat(EnclosedType.class.getCanonicalName()).isEqualTo("com.github.wreulicke.reflect.ClassTest.EnclosedType");
    assertThat(ClassTest.class.getSimpleName()).isEqualTo("ClassTest");
    assertThat(EnclosedType.class.getSimpleName()).isEqualTo("EnclosedType");

    assertThat(ClassTest.class.getTypeName()).isEqualTo("com.github.wreulicke.reflect.ClassTest");
    assertThat(ClassTest.class.getTypeParameters()).isEmpty();
  }

  @SafeVarargs
  public static <T> Class<?> clazzes(T... arrays) {
    return arrays.getClass()
      .getComponentType();
  }

  /**
   * 配列とクラスの関係編
   */
  @Test
  public void test2() {
    Class<?> clazz1 = ClassTest.class;
    Class<?> clazz2 = ClassTest[].class;

    assertThat(clazz2.getComponentType()).isEqualTo(clazz1);
    assertThat(clazz1.isArray()).isFalse();
    assertThat(clazz2.isArray()).isTrue();
    // 良い子のみんなは使っちゃダメ。
    assertThat(clazzes(new ClassTest())).isEqualTo(ClassTest.class);
  }


  /**
   * 合成クラスってなんやねん
   * 
   * @throws IllegalAccessException
   * @throws NoSuchFieldException
   */
  @Test
  public void test3() throws NoSuchFieldException, IllegalAccessException {
    // もちろん合成クラスじゃない
    assertThat(ClassTest.class.isSynthetic()).isFalse();
    assertThat(EnclosedType.class.isSynthetic()).isFalse();
    assertThat(new Object() {}.getClass()
      .isSynthetic()).isFalse();
    assertThat(new EnclosedType().new Inner().getClass()
      .isSynthetic()).isFalse();
    assertThat(ClassTest[].class.isSynthetic()).isFalse();

    // Proxyは合成クラスじゃない
    assertThat(Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[] {
      Predicate.class
    }, (proxy, name, args) -> null)
      .getClass()
      .isSynthetic()).isFalse();

    // ラムダ式は合成クラス
    assertThat(((Predicate<Integer>) (Integer i) -> true).getClass()
      .isSynthetic()).isTrue();

    // 似たようなメソッドで生成したクラスは合成クラスではない
    Lookup lookup = MethodHandles.lookup();
    MethodHandle handle = lookup.findStaticGetter(System.class, "out", PrintStream.class);
    assertThat(MethodHandleProxies.asInterfaceInstance(Supplier.class, handle)
      .getClass()
      .isSynthetic()).isFalse();
  }

  /**
   * キャストとかその辺
   */
  @Test
  public void test4() {
    ClassTest.class.cast(new ClassTest());
    ClassTest.class.isAssignableFrom(new ClassTest().getClass());
    new ClassTest() {}.getClass()
      .asSubclass(ClassTest.class);
  }


}
