package com.github.wreulicke.reflect.doc1;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Bar {

  public String bar() {
    return "Barだよ";
  }

  public String barWithParameter(String str) {
    return str;
  }
}

public class MethodTest {

  @Test
  public void getMethod() throws Exception {
    // メソッドの取得が出来る
    final Method method = Bar.class.getMethod("bar");
    assertThat(method).isNotNull();
  }

  @Test
  public void getMethod_barWithParameter() throws Exception {
    // 引数があるメソッドを取得する場合は引数にマッチするクラスを渡してあげないといけない
    final Method method = Bar.class.getMethod("barWithParameter", String.class);
    assertThat(method).isNotNull();
  }

  @Test
  public void invokeMethod() throws Exception {
    // メソッドの呼び出しが出来る
    Bar bar = new Bar();
    final Method method = Bar.class.getMethod("bar");
    assertThat(method.invoke(bar)).isEqualTo("Barだよ");
  }

  @Test
  public void invokeMethod_barWithParameter() throws Exception {
    // 引数があるメソッドを呼び出すときは、実引数を渡してあげないといけない
    Bar bar = new Bar();
    final Method method = Bar.class.getMethod("barWithParameter", String.class);
    assertThat(method.invoke(bar, "parameter")).isEqualTo("parameter");
  }

}
