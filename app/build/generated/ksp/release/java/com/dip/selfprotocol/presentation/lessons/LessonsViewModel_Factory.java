package com.dip.selfprotocol.presentation.lessons;

import androidx.lifecycle.SavedStateHandle;
import com.dip.selfprotocol.data.local.dao.CategoryDao;
import com.dip.selfprotocol.data.local.dao.LessonDao;
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
public final class LessonsViewModel_Factory implements Factory<LessonsViewModel> {
  private final Provider<LessonDao> lessonDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public LessonsViewModel_Factory(Provider<LessonDao> lessonDaoProvider,
      Provider<CategoryDao> categoryDaoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.lessonDaoProvider = lessonDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public LessonsViewModel get() {
    return newInstance(lessonDaoProvider.get(), categoryDaoProvider.get(), savedStateHandleProvider.get());
  }

  public static LessonsViewModel_Factory create(Provider<LessonDao> lessonDaoProvider,
      Provider<CategoryDao> categoryDaoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new LessonsViewModel_Factory(lessonDaoProvider, categoryDaoProvider, savedStateHandleProvider);
  }

  public static LessonsViewModel newInstance(LessonDao lessonDao, CategoryDao categoryDao,
      SavedStateHandle savedStateHandle) {
    return new LessonsViewModel(lessonDao, categoryDao, savedStateHandle);
  }
}
