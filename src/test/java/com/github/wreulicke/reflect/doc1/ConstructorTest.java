package com.github.wreulicke.reflect.doc1;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

class Baz {

  public final String field;

  public Baz() {
    field = "default";
  }

  public Baz(String str) {
    field = str;
  }

}

public class ConstructorTest {

  @Test
  public void getConstructor() throws Exception {
    // コンストラクタの取得ができる
    Constructor<Baz> constructor = Baz.class.getConstructor();
    assertThat(constructor).isNotNull();
  }

  @Test
  public void getConstructorWithParameter() throws Exception {
    // 指定したClassオブジェクトの並びに対応する、コンストラクタが取得できる
    Constructor<Baz> constructor = Baz.class.getConstructor(String.class);
    assertThat(constructor).isNotNull();
  }

  @Test
  public void newInstance() throws Exception {
    Constructor<Baz> constructor = Baz.class.getConstructor();
    // コンストラクタが呼び出されて フィールドに、"default" が設定されている
    assertThat(constructor.newInstance().field).isEqualTo("default");
  }

  @Test
  public void newInstanceWithParameter() throws Exception {
    Constructor<Baz> constructor = Baz.class.getConstructor(String.class);
    // コンストラクタが呼び出されて フィールドに、引数で渡されてきた "test" が設定されている
    assertThat(constructor.newInstance("test").field).isEqualTo("test");
  }


}
