package com.rmakiyama.daggerhiltstudy;

import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = HiltStudyApp.class
)
@GeneratedEntryPoint
@InstallIn(SingletonComponent.class)
public interface HiltStudyApp_GeneratedInjector {
  void injectHiltStudyApp(HiltStudyApp hiltStudyApp);
}
