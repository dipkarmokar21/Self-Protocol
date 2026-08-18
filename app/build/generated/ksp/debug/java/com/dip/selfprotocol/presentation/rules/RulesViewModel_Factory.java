package com.dip.selfprotocol.presentation.rules;

import androidx.lifecycle.SavedStateHandle;
import com.dip.selfprotocol.data.local.dao.CategoryDao;
import com.dip.selfprotocol.data.local.dao.RuleDao;
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
public final class RulesViewModel_Factory implements Factory<RulesViewModel> {
  private final Provider<RuleDao> ruleDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public RulesViewModel_Factory(Provider<RuleDao> ruleDaoProvider,
      Provider<CategoryDao> categoryDaoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.ruleDaoProvider = ruleDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public RulesViewModel get() {
    return newInstance(ruleDaoProvider.get(), categoryDaoProvider.get(), savedStateHandleProvider.get());
  }

  public static RulesViewModel_Factory create(Provider<RuleDao> ruleDaoProvider,
      Provider<CategoryDao> categoryDaoProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new RulesViewModel_Factory(ruleDaoProvider, categoryDaoProvider, savedStateHandleProvider);
  }

  public static RulesViewModel newInstance(RuleDao ruleDao, CategoryDao categoryDao,
      SavedStateHandle savedStateHandle) {
    return new RulesViewModel(ruleDao, categoryDao, savedStateHandle);
  }
}
