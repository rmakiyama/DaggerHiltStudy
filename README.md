# Step1. Hiltをアプリケーションに適用する。

導入方法は割愛です。

まずは`@HiltAndroidApp`アノテーションをつけるのみで様子を見てみます。

```kotlin
package com.rmakiyama.daggerhiltstudy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltStudyApp : Application()

```

こちらをビルドすると、以下のファイルが自動生成されます。

- Hilt_HiltStudyApp
- DaggerHiltStudyApp_HiltComponents_SingletonC
- HiltStudyApp_GeneratedInjector
- HiltStudyApp_HiltComponents
- com_rmakiyama_daggerhiltstudy_HiltStudyApp_GeneratedInjectorModuleDeps

それぞれについて見ていきましょう。

## Hilt_HiltStudyApp

どうやら`Hilt_${application_name}`なクラスが生成されるようです。Androidの`Application`クラスを継承しています。

クラスを見てみると`Hilt_HiltStudyApp`はHiltライブラリの持つクラスである`ApplicationComponentManager`を内部で保持していました。見るからにApllicaionのComponentをManagerしてくれそうですね。

```java
  ...
  private final ApplicationComponentManager componentManager = new ApplicationComponentManager(new ComponentSupplier() {
    @Override
    public Object get() {
      return DaggerHiltStudyApp_HiltComponents_SingletonC.builder()
          .applicationContextModule(new ApplicationContextModule(Hilt_HiltStudyApp.this))
          .build();
    }
  });
  ...
```

`ComponentSupplier`を使ってComponentを生成しているようです。`ComponentSupplier`は`get`メソッドを持つただのインタフェースでした。  
生成している`DaggerHiltStudyApp_HiltComponents_SingletonC`はアノテーションにより生成されたクラスです。これまでDagger2を使ったことのある人は見覚えのある記述ではないでしょうか。これがアプリケーションレベルのコンポーネントのようです。  
`ApplicationContextModule`なんてのもこのコンポーネントに指定しているようです。これはHilt内部クラスで、どうやら`ApplicatinContext`をオブジェクトグラフに登録しているようですね。

その後、`Application`の`onCreate`をオーバーライドし、HiltStudyAppがコンポーネントにアクセスできるよう`injectHiltStudyApp`を呼び出しています。

```java
  ...
  @CallSuper
  @Override
  public void onCreate() {
    // This is a known unsafe cast, but is safe in the only correct use case:
    // HiltStudyApp extends Hilt_HiltStudyApp
    ((HiltStudyApp_GeneratedInjector) generatedComponent()).injectHiltStudyApp(UnsafeCasts.<HiltStudyApp>unsafeCast(this));
    super.onCreate();
  }
  ...
```

ここで出た`HiltStudyApp_GeneratedInjector`は、アノテーションにより生成されたインタフェースです。`DaggerHiltStudyApp_HiltComponents_SingletonC`が実装しています。

`generatedComponent`は`Hilt_HiltStudyApp`が実装しています。`ApplicationComponentManager`内部のコンポーネント、つまり`DaggerHiltStudyApp_HiltComponents_SingletonC`を返す実装になっています。

## HiltStudyApp_HiltDaggerHiltStudyApp_HiltComponents_SingletonCComponents

次にこれです。前述の通り、生成された`Hilt_HiltStudyApp`の持つ`ApplicationComponentManager`が内部で持っているコンポーネントになります。

このクラスは内部に自身のビルダークラス、ViewModelレベルのコンポーネントとそのビルダークラス、サービスレベルのコンポーネントとそのビルダークラスが定義されていました。

```java
public final class DaggerHiltStudyApp_HiltComponents_SingletonC extends HiltStudyApp_HiltComponents.SingletonC {
  ...
  public static final class Builder { ... }
  private final class ActivityRetainedCBuilder ...
  private final class ActivityRetainedCImpl ...
  private final class ServiceCBuilder ...
  private final class ServiceCImpl ...
}
```

さらに、ViewModelレベルのコンポーネントクラスの内部では、アクティビティレベルのコンポーネントとそのビルダークラスが定義されています。さらに…といった具合に、[Hiltの定義したコンポーネント階層](https://developer.android.com/training/dependency-injection/hilt-android?hl=ja#component-hierarchy)に沿ってコードが生成されていました。おもしろい。

各レベルのコンポーネントは、`HiltStudyApp_HiltComponents`に定義された抽象クラスを継承しているようです。見やすいので後ほどかんたんに説明します。

## HiltStudyApp_GeneratedInjector

前述したように、`DaggerHiltStudyApp_HiltComponents_SingletonC`により実装されているインタフェースです。`injectHiltStudyApp`のみ定義されています。

これをアプリケーションクラスが呼び出すことにより、アプリケーションレベルのコンポーネントに`HiltStudyApp`がアクセスできるようになります。(自動生成で`Hilt_HiltStudyApp`がやっている。)

## HiltStudyApp_HiltComponents

アプリケーションにおけるHiltのコンポーネント定義がこちらにまとまっています。ここでコンポーネント階層の標準化がなされているようです。

`@Component`アノテーションがつくのは唯一`HiltStudyApp_HiltComponents.SingletonC`クラスのみです。

```java
  ...
  @Component(
      modules = {
          ApplicationContextModule.class,
          ActivityRetainedCBuilderModule.class,
          ServiceCBuilderModule.class
      }
  )
  @Singleton
  public abstract static class SingletonC implements HiltStudyApp_GeneratedInjector,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint,
      ServiceComponentManager.ServiceComponentBuilderEntryPoint,
      SingletonComponent,
      GeneratedComponent {
  }
  ...
```

`SingletonComponent`のデフォルトのバインディングであるアプリケーションを提供する`ApplicationContextModule`の他、コンポーネント階層に従って、`ActivityRetainedCBuilderModule`/`ServiceCBuilderModule`がコンポーネントに指定されています。

その他のコンポーネントはそれぞれ`@Subcomponent`アノテーションがついたサブコンポーネントとして定義されています。そして、デフォルトのバインディングを提供するモジュールと、コンポーネント階層に従ったモジュールが指定されています。  
以下は`ActivityComponent`の例です。

```java
  @Subcomponent(
      modules = {
          FragmentCBuilderModule.class,
          ViewCBuilderModule.class,
          HiltWrapper_ActivityModule.class,
          HiltWrapper_DefaultViewModelFactories_ActivityModule.class
      }
  )
  @ActivityScoped
  public abstract static class ActivityC implements ActivityComponent,
      DefaultViewModelFactories.ActivityEntryPoint,
      FragmentComponentManager.FragmentComponentBuilderEntryPoint,
      ViewComponentManager.ViewComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityComponentBuilder {
    }
  }
```

こうして、Hiltではアクティビティごとに1つではなく、唯一1つのアクティビティコンポーネントを持つことがわかります。

## com_rmakiyama_daggerhiltstudy_HiltStudyApp_GeneratedInjectorModuleDeps

`@HiltAndroidApp`アノテーションをアプリケーションで定義しただけの段階では以下のようなコードが生成されました。

```java
/**
 * Generated class to pass information through multiple javac runs.
 */
@AggregatedDeps(
    components = "dagger.hilt.components.SingletonComponent",
    entryPoints = "com.rmakiyama.daggerhiltstudy.HiltStudyApp_GeneratedInjector"
)
class com_rmakiyama_daggerhiltstudy_HiltStudyApp_GeneratedInjectorModuleDeps {
}
```

コメントにあるように、コンパイラに生成クラスのパスを知らせるためのクラスかなという理解までしかできませんでした…。一旦保留。

[Step2に進む👉🏻](https://github.com/rmakiyama/DaggerHiltStudy/tree/step-2_add-binding)
