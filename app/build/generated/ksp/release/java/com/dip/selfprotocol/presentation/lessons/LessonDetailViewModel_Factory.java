package com.dip.selfprotocol.presentation.lessons;

import androidx.lifecycle.SavedStateHandle;
import com.dip.selfprotocol.data.local.dao.LessonDao;
import com.dip.selfprotocol.util.DraftManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class LessonDetailViewModel_Factory implements Factory<LessonDetailViewModel> {
  private final Provider<LessonDao> lessonDaoProvider;

  private final Provider<DraftManager> draftManagerProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public LessonDetailViewModel_Factory(Provider<LessonDao> lessonDaoProvider,
      Provider<DraftManager> draftManagerProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.lessonDaoProvider = lessonDaoProvider;
    this.draftManagerProvider = draftManagerProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public LessonDetailViewModel get() {
    return newInstance(lessonDaoProvider.get(), draftManagerProvider.get(), savedStateHandleProvider.get());
  }

  public static LessonDetailViewModel_Factory create(Provider<LessonDao> lessonDaoProvider,
      Provider<DraftManager> draftManagerProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new LessonDetailViewModel_Factory(lessonDaoProvider, draftManagerProvider, savedStateHandleProvider);
  }

  public static LessonDetailViewModel newInstance(LessonDao lessonDao, DraftManager draftManager,
      SavedStateHandle savedStateHandle) {
    return new LessonDetailViewModel(lessonDao, draftManager, savedStateHandle);
  }
}
