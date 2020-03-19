package com.github.wreulicke.reflect.doc2;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.TypeVariable;

import static org.assertj.core.api.Assertions.assertThat;

@Retention(RetentionPolicy.RUNTIME)
@Target({
  ElementType.TYPE,
  ElementType.METHOD,
  ElementType.FIELD,
  ElementType.TYPE_PARAMETER,
  ElementType.PARAMETER,
  ElementType.TYPE_USE,
  ElementType.LOCAL_VARIABLE })
@interface Foo {
  String value() default "";
}

@Foo("class")
class Bar<@Foo("class-type-parameter") T> {

  @Foo("field")
  private final Bar<@Foo("type-argument") T> foo = new Bar<>();

  @Foo
  public <@Foo("method-type-parameter") X> String bar(
      @Foo("receiver") Bar<T> this,
      @Foo("parameter") Bar<?> bar) throws @Foo("throws") Exception {

    final @Foo("local") Exception e = new Exception("test");
    throw e;
  }

}


public class AnnotationTest {

  @Test
  public void クラスのアノテーションを取得する() {
    assertThat(Bar.class.getAnnotation(Foo.class)).isNotNull();
  }

  @Test
  public void フィールドのアノテーションを取得する() throws NoSuchFieldException {
    assertThat(Bar.class.getDeclaredField("foo").getAnnotation(Foo.class)).isNotNull();
  }

  @Test
  public void メソッドの戻り値に対するアノテーションを取得する() throws NoSuchMethodException {
    assertThat(Bar.class.getMethod("bar", Bar.class).getAnnotatedReturnType().getAnnotation(Foo.class)).isNotNull();
  }

  @Test
  public void メソッドの引数のアノテーションを取得する() throws NoSuchMethodException {
    assertThat(Bar.class.getMethod("bar", Bar.class).getAnnotatedParameterTypes()[0].getAnnotation(Foo.class))
        .isNotNull()
        .satisfies(annotation -> {
          assertThat(annotation.value()).isEqualTo("parameter");
        });
  }

  @Test
  public void 型パラメータのアノテーションを取得する() {
    final TypeVariable<Class<Bar>> typeVariable = Bar.class.getTypeParameters()[0];
    assertThat(typeVariable).isInstanceOf(TypeVariable.class);
    assertThat(typeVariable.getAnnotation(Foo.class))
        .isNotNull();
  }

  @Test
  public void フィールドの実型パラメータを取得する() throws NoSuchFieldException {
    assertThat(Bar.class.getDeclaredField("foo").getAnnotatedType().getAnnotation(Foo.class)).isNotNull();
  }

  @Test
  public void メソッドのレシーバパラメータのアノテーションを取得する() throws NoSuchMethodException {
    assertThat(Bar.class.getMethod("bar", Bar.class).getAnnotatedReceiverType().getAnnotation(Foo.class))
        .isNotNull()
        .satisfies(annotation -> {
          assertThat(annotation.value()).isEqualTo("receiver");
        });
  }

  @Test
  public void メソッドの型パラメータのアノテーションを取得する() throws NoSuchMethodException {
    assertThat(Bar.class.getMethod("bar", Bar.class).getTypeParameters()[0].getAnnotation(Foo.class))
        .isNotNull()
        .satisfies(annotation -> {
          assertThat(annotation.value()).isEqualTo("method-type-parameter");
        });
  }

  @Test
  public void メソッドのthrows句のアノテーションを取得する() throws NoSuchMethodException {
    assertThat(Bar.class.getMethod("bar", Bar.class).getAnnotatedExceptionTypes()[0].getAnnotation(Foo.class))
        .isNotNull()
        .satisfies(annotation -> {
          assertThat(annotation.value()).isEqualTo("throws");
        });
  }
}
