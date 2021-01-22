# Step2. オブジェクトグラフにバインディングを登録する。

次に、オブジェクトグラフにバインディングを登録してみます。

こちらは`@Inject`を使った登録。

```kotlin
class Hoge @Inject constructor()
```

こちらは`@Inject`を使った登録に`@Singleton`スコープを付与。

```kotlin
@Singleton
class Fuga @Inject constructor()
```

こちらはインタフェースを定義します。

```kotlin
interface Piyo

class PiyoImpl : Piyo
```

そして、モジュールを使って`SingletonComponent`に登録。

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object HiltStudyAppModule {

    @Provides
    fun providePiyo(): Piyo {
        return PiyoImpl()
    }
}
```

さらにこれらを`HiltStudyApp`にメンバー変数としてインジェクトしてみます。

```kotlin
@HiltAndroidApp
class HiltStudyApp : Application() {
    @Inject lateinit var hoge: Hoge
    @Inject lateinit var fuga: Fuga
    @Inject lateinit var piyo: Piyo
}
```

ここでビルドすると以下のクラスが生成されました。

- Hoge_Factory
- Fuga_Factory
- HiltStudyAppModule_ProvidePiyoFactory
- HiltStudyApp_MembersInjector

また、以下のクラスにコードが追加されました。

- DaggerHiltStudyApp_HiltComponents_SingletonC
- HiltStudyApp_HiltComponents

## それぞれのFactoryクラス

オブジェクトグラフに登録したクラスは、それぞれファクトリーが生成されました。  
それぞれのファクトリークラスは、Daggerの`Provider<T>`の`get`を実装していて、対象のクラスのインスタンスを生成しています。

`Hoge_Factory`/`Fuga_Factory`は、引数を取るものではないため単純にデフォルトのコンストラクタを呼ぶことで生成されます。

`HiltStudyAppModule_ProvidePiyoFactory`は、`HiltStudyAppModule`に`@Provides`をつけて定義した関数を用いてインスタンスを生成しています。

## HiltStudyApp_MembersInjector

名前の通り、それぞれのインスタンスをメンバー変数としてインジェクトするためのクラスです。
`Hoge`/`Fuga`/`Piyo`についてそれぞれプロバイダを保持しています。  
これらのプロバイダは、`create`関数によってDagger内部で生成されるようです。

また、それぞれを実際にメンバー変数としてインジェクトするためのメソッドとして、`injectHoge`/`injectFuga`/`injectPiyo`が定義されています。  
たとえば、`injectHoge`は以下のように定義されます。

```java
  public static void injectHoge(HiltStudyApp instance, Hoge hoge) {
    instance.hoge = hoge;
  }
```

引数として、インジェクトする先のインスタンスと、インジェクトするクラスのインスタンスが渡されています。  
そしてこれらは、`DaggerHiltStudyApp_HiltComponents_SingletonC`から呼び出されていました。

## DaggerHiltStudyApp_HiltComponents_SingletonCの変更

Step1で説明したように、`Hilt_HiltStudyApp`はアプリケーションレベルのコンポーネントに対して(つまり`DaggerHiltStudyApp_HiltComponents_SingletonCの変更`に対して)、`injectHiltStudyApp()`メソッドで、注入を要求するインスタンス(`HiltStudyApp`)を渡しています。

Step1までは、このメソッドはからの実装でしたが、ここに以下のようにコードが追加されます。

```java
  ...
  @Override
  public void injectHiltStudyApp(HiltStudyApp hiltStudyApp) {
    injectHiltStudyApp2(hiltStudyApp);
  }

  private HiltStudyApp injectHiltStudyApp2(HiltStudyApp instance) {
    HiltStudyApp_MembersInjector.injectHoge(instance, new Hoge());
    HiltStudyApp_MembersInjector.injectFuga(instance, fuga());
    HiltStudyApp_MembersInjector.injectPiyo(instance, HiltStudyAppModule_ProvidePiyoFactory.providePiyo());
    return instance;
  }
  ...
```

`injectHiltStudyApp2()`メソッドが生成され、`HiltStudyApp_MembersInjector`の各メソッドを呼び出し、メンバー変数にインスタンスを注入していることがわかります。

ここで、`@Singleton`スコープをつけて定義した`Fuga`クラスのインスタンスは`fuga()`メソッドで取得していることに気づきます。  
このメソッドは別途、以下のように追加されていました。

```java
  ...
  private volatile Object fuga = new MemoizedSentinel();
  ...
  private Fuga fuga() {
    Object local = fuga;
    if (local instanceof MemoizedSentinel) {
      synchronized (local) {
        local = fuga;
        if (local instanceof MemoizedSentinel) {
          local = new Fuga();
          fuga = DoubleCheck.reentrantCheck(fuga, local);
        }
      }
    }
    return (Fuga) local;
  }
  ...
```

細かい説明は省きますが、一度生成したインスタンスを使いまわしていることがわかります。指定したスコープ通り、シングルトンなインスタンスが注入されていますね。

## HiltStudyApp_HiltComponentsの変更

`HiltStudyApp_HiltComponents`については1行のみ追加されていました。

```java
  ...
  @Component(
      modules = {
          ApplicationContextModule.class,
+         HiltStudyAppModule.class,
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

Dagger2を使ったことがある人は、モジュールを追加するたびにそれをコンポーネントへ指定していたことを思い出すでしょう。Hiltではこのようにしてこれを自動でやってくれます。便利。

モジュールを追加して`@InstallIn`アノテーションをつけるだけで指定のコンポーネントのオブジェクトグラフにバインディング登録される謎がこれで解けました。

## その他

`com_rmakiyama_daggerhiltstudy_HiltStudyAppModuleModuleDeps`クラスも生成されていましたが、割愛します。Step1同様、コンパイラのためのクラスでしょうか。

[Step3に進む👉🏻](https://github.com/rmakiyama/DaggerHiltStudy/tree/step-3_add-AndroidEntryPoint)
