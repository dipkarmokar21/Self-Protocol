package com.dip.selfprotocol.presentation.categories;

import androidx.lifecycle.SavedStateHandle;
import com.dip.selfprotocol.data.local.dao.CategoryDao;
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
public final class CategoriesViewModel_Factory implements Factory<CategoriesViewModel> {
  private final Provider<CategoryDao> categoryDaoProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public CategoriesViewModel_Factory(Provider<CategoryDao> categoryDaoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.categoryDaoProvider = categoryDaoProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public CategoriesViewModel get() {
    return newInstance(categoryDaoProvider.get(), savedStateHandleProvider.get());
  }

  public static CategoriesViewModel_Factory create(Provider<CategoryDao> categoryDaoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new CategoriesViewModel_Factory(categoryDaoProvider, savedStateHandleProvider);
  }

  public static CategoriesViewModel newInstance(CategoryDao categoryDao,
      SavedStateHandle savedStateHandle) {
    return new CategoriesViewModel(categoryDao, savedStateHandle);
  }
}
