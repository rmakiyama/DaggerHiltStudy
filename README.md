# Step3. アクティビティにAndroidEntryPointアノテーションをつける。

まずは`@AndroidEntryPoint`アノテーションを`MainActivity`につけるのみで様子を見てみます。

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

この状態でビルドすると、以下のファイルが自動生成されます。

- Hilt_MainActivity
- MainActivity_GeneratedInjector

そして、以下のクラスにコードが追加されました。

- DaggerHiltStudyApp_HiltComponents_SingletonC
- HiltStudyApp_HiltComponents

なにやら見覚えがありますね。それぞれを軽く見ていきます。

## Hilt_MainActivity

`Hilt_MainActivity`はHiltライブラリの持つクラスである`ActivityComponentManager`を内部で保持していました。  
概ね実装は`Hilt_HiltStudyApp`と同じパターンです。

`ActivityComponentManager`は`ApplicationComponentManager`と違い、`generatedComponent()`メソッドで取得するコンポーネントがシングルトンになっているという点です。前述したように、Hiltでは1つのアクティビティコンポーネントを使用してすべてのアクティビティを注入するからです。

また、`Hilt_MainActivity`には`ViewModelProvider.Factory`を取得するメソッドも定義されていました。

## MainActivity_GeneratedInjector

アクティビティコンポーネントに`MainActivity`がアクセスできるように`injectMainActivity()`メソッドがインタフェースとして定義しています。  
ここもアプリケーションのときと同じ流れですね。

## DaggerHiltStudyApp_HiltComponents_SingletonCの変更

アクティビティコンポーネントに`MainActivity`がアクセスできるようにするメソッドが追加されたのみでした。

```java
+     @Override
+     public void injectMainActivity(MainActivity mainActivity) {
+     }
```

## HiltStudyApp_HiltComponentsの変更

アクティビティコンポーネントが`MainActivity_GeneratedInjector`を実装するような追加がされたのみでした。

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
+ public abstract static class ActivityC implements MainActivity_GeneratedInjector,
+     ActivityComponent,
      DefaultViewModelFactories.ActivityEntryPoint,
      FragmentComponentManager.FragmentComponentBuilderEntryPoint,
      ViewComponentManager.ViewComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityComponentBuilder {
    }
  }
```

[Step4に進む👉🏻](https://github.com/rmakiyama/DaggerHiltStudy/tree/step-4_add-activity-member)
