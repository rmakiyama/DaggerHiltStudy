// Generated by Dagger (https://dagger.dev).
package com.rmakiyama.daggerhiltstudy;

import dagger.MembersInjector;
import dagger.internal.InjectedFieldSignature;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<Hoge> hogeProvider;

  private final Provider<HogeHoge> hogeHogeProvider;

  private final Provider<Fuga> fugaProvider;

  private final Provider<FugaFuga> fugaFugaProvider;

  private final Provider<Piyo> piyoProvider;

  private final Provider<PiyoPiyo> piyoPiyoProvider;

  public MainActivity_MembersInjector(Provider<Hoge> hogeProvider,
      Provider<HogeHoge> hogeHogeProvider, Provider<Fuga> fugaProvider,
      Provider<FugaFuga> fugaFugaProvider, Provider<Piyo> piyoProvider,
      Provider<PiyoPiyo> piyoPiyoProvider) {
    this.hogeProvider = hogeProvider;
    this.hogeHogeProvider = hogeHogeProvider;
    this.fugaProvider = fugaProvider;
    this.fugaFugaProvider = fugaFugaProvider;
    this.piyoProvider = piyoProvider;
    this.piyoPiyoProvider = piyoPiyoProvider;
  }

  public static MembersInjector<MainActivity> create(Provider<Hoge> hogeProvider,
      Provider<HogeHoge> hogeHogeProvider, Provider<Fuga> fugaProvider,
      Provider<FugaFuga> fugaFugaProvider, Provider<Piyo> piyoProvider,
      Provider<PiyoPiyo> piyoPiyoProvider) {
    return new MainActivity_MembersInjector(hogeProvider, hogeHogeProvider, fugaProvider, fugaFugaProvider, piyoProvider, piyoPiyoProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectHoge(instance, hogeProvider.get());
    injectHogeHoge(instance, hogeHogeProvider.get());
    injectFuga(instance, fugaProvider.get());
    injectFugaFuga(instance, fugaFugaProvider.get());
    injectPiyo(instance, piyoProvider.get());
    injectPiyoPiyo(instance, piyoPiyoProvider.get());
  }

  @InjectedFieldSignature("com.rmakiyama.daggerhiltstudy.MainActivity.hoge")
  public static void injectHoge(MainActivity instance, Hoge hoge) {
    instance.hoge = hoge;
  }

  @InjectedFieldSignature("com.rmakiyama.daggerhiltstudy.MainActivity.hogeHoge")
  public static void injectHogeHoge(MainActivity instance, HogeHoge hogeHoge) {
    instance.hogeHoge = hogeHoge;
  }

  @InjectedFieldSignature("com.rmakiyama.daggerhiltstudy.MainActivity.fuga")
  public static void injectFuga(MainActivity instance, Fuga fuga) {
    instance.fuga = fuga;
  }

  @InjectedFieldSignature("com.rmakiyama.daggerhiltstudy.MainActivity.fugaFuga")
  public static void injectFugaFuga(MainActivity instance, FugaFuga fugaFuga) {
    instance.fugaFuga = fugaFuga;
  }

  @InjectedFieldSignature("com.rmakiyama.daggerhiltstudy.MainActivity.piyo")
  public static void injectPiyo(MainActivity instance, Piyo piyo) {
    instance.piyo = piyo;
  }

  @InjectedFieldSignature("com.rmakiyama.daggerhiltstudy.MainActivity.piyoPiyo")
  public static void injectPiyoPiyo(MainActivity instance, PiyoPiyo piyoPiyo) {
    instance.piyoPiyo = piyoPiyo;
  }
}
