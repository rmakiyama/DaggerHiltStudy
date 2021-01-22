AndroidにおけるDIは、Dagger Hiltの登場により、より一層ボイラープレートが減り定義しやすくなりました。  開発者としては非常に嬉しく、個人・プロダクトでも導入しています。  
この最高の黒魔術でどのようにしてDIを行っているのか、それを少しでも知る旅に出るのでした。

※ 概ね自分の理解と記憶のために書いているので、詳細は省いている場合があります。また、間違った理解があったりした場合は教えていただけると嬉しいです！

# 対象読者

- Daggerをなんとなく理解している
- Hiltを使ったことがある/調べたことがある
- Dagger Hiltが何しているか気になる

# 前提

環境はこちら。

- Android Studio 4.2 Beta 3
- Dagger Hilt 2.31.2-alpha

今回はAndroidのアプリケーションクラスを継承したクラスを`HiltStudyApp`とします。

[Step1に進む👉🏻](https://github.com/rmakiyama/DaggerHiltStudy/tree/step-1_add-HiltAndroidApp)
