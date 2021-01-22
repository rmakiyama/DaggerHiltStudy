# Step4. アクティビティにインスタンスを注入する。

さらにオブジェクトグラフにバインディングを登録してみます。

こちらは`@Inject`を使った登録。

```kotlin
class HogeHoge @Inject constructor(
    private val hoge: Hoge,
)
```

こちらは`@Inject`を使った登録に`@ActivityScoped`スコープを付与。

```kotlin
@ActivityScoped
class FugaFuga @Inject constructor(
    private val fuga: Fuga,
)
```

こちらはインタフェースを定義します。

```kotlin
interface PiyoPiyo

class PiyoPiyoImpl @Inject constructor(
    private val piyo: Piyo,
) : PiyoPiyo
```

そして、モジュールを使って`ActivityComponent`に登録。

```kotlin
@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {

    @Provides
    fun providePiyoPiyo(
        piyo: Piyo,
    ): PiyoPiyo {
        return PiyoPiyoImpl(piyo)
    }
}
```

さらにこれらを`MainActivity`にメンバー変数としてインジェクトしてみます。  
ついでに`Hoge`や`Fuga`もインジェクトしてみましょう。

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var hoge: Hoge
    @Inject lateinit var hogeHoge: HogeHoge
    @Inject lateinit var fuga: Fuga
    @Inject lateinit var fugaFuga: FugaFuga
    @Inject lateinit var piyo: Piyo
    @Inject lateinit var piyoPiyo: PiyoPiyo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

ここでビルドすると、前回同様、追加したクラスのファクトリークラスとメンバーインジェクターが生成されました。  
また`DaggerHiltStudyApp_HiltComponents_SingletonC`/`HiltStudyApp_HiltComponents`にコードが追加されていました。

さて、ここまでくると、なんとなく頭の中でHiltの生成するコードが浮かんでくるのではないでしょうか？

ぜひ頭の中で思い浮かべて、ソースコードをご確認ください！

# まとめ

かんたんなHiltの連携とメンバー変数のインジェクトをした際の、Hiltが生成するコードについて見ていきました。  
どんなコードが生成されているかを理解すると気持ちいいですね。Hiltの気持ちを少しわかった気がします。エラー文とも仲良くなれる気がします。

# 参考

- [Master of Dagger あんざいゆき第2版【技術書典9 新刊】](https://booth.pm/ja/items/1577764)
- [Dagger の基本](https://developer.android.com/training/dependency-injection/dagger-basics?hl=ja)
- [Android アプリで Dagger を使用する](https://developer.android.com/training/dependency-injection/dagger-android?hl=ja)
- [Hilt を使用した依存関係の注入](https://developer.android.com/training/dependency-injection/hilt-android?hl=ja)
