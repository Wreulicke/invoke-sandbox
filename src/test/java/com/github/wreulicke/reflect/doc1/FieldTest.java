package com.github.wreulicke.reflect.doc1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class Foo {
  public String name = "Foo";

  private String privateName = "privateName";
}

public class FieldTest {

  @Test
  public void getField() throws Exception {
    // フィールドが取得できる
    Field nameField = Foo.class.getField("name");
    assertThat(nameField).isNotNull();
  }

  @Test
  public void getFieldValue() throws Exception {
    // フィールドの値が取得できる
    Foo foo = new Foo();
    Field field = Foo.class.getField("name");
    assertThat(field.get(foo)).isEqualTo("Foo");
  }

  @Test
  public void setFieldValue() throws Exception {
    // フィールドの値が設定できる
    Foo foo = new Foo();
    Field field = Foo.class.getField("name");
    assertThat(field.get(foo)).isEqualTo("Foo"); // この時点ではFoo
    field.set(foo, "Bar");
    assertThat(field.get(foo)).isEqualTo("Bar"); // 変化している！
  }

  @Test
  public void getPrivateField_FailedCase() throws Exception {
    // アクセスできないフィールドに対して getFieldを呼び出すとNoSuchFieldExceptionが帰ってくる
    assertThatThrownBy(() -> Foo.class.getField("privateName"))
        .isInstanceOf(NoSuchFieldException.class);
  }

  @Test
  public void getPrivateField() throws Exception {
    // private フィールドのFieldオブジェクトが取得できる
    Field privateNameField = Foo.class.getDeclaredField("privateName");
    assertThat(privateNameField).isNotNull();
  }

  @Test
  public void getPrivateFieldValue_FailedCase() throws Exception {
    // private フィールドの取得ができない
    Foo foo = new Foo();
    Field privateNameField = Foo.class.getDeclaredField("privateName");
    assertThatThrownBy(() -> privateNameField.get(foo))
        .isInstanceOf(IllegalAccessException.class);
  }

  @Test
  public void setPrivateFieldValue_FailedCase() throws Exception {
    // private フィールドの設定ができない
    Foo foo = new Foo();
    Field privateNameField = Foo.class.getDeclaredField("privateName");
    assertThatThrownBy(() -> privateNameField.set(foo, "Bar"))
        .isInstanceOf(IllegalAccessException.class);
  }

  @Test
  public void getPrivateFieldValue() throws Exception {
    // private フィールドの取得が出来る
    Foo foo = new Foo();
    Field privateNameField = Foo.class.getDeclaredField("privateName");
    privateNameField.setAccessible(true);
    privateNameField.get(foo); // setAccessibleを呼び出したことで、get出来るようになった
  }

  @Test
  public void setPrivateFieldValue() throws Exception {
    // private フィールドの設定が出来る
    Foo foo = new Foo();
    Field privateNameField = Foo.class.getDeclaredField("privateName");
    privateNameField.setAccessible(true);
    privateNameField.set(foo, "Bar"); // setAccessibleを呼び出したことで、set出来るようになった
  }
}
