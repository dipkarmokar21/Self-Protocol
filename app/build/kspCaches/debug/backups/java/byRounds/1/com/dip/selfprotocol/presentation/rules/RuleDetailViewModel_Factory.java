package com.dip.selfprotocol.presentation.rules;

import androidx.lifecycle.SavedStateHandle;
import com.dip.selfprotocol.data.local.dao.RuleDao;
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
public final class RuleDetailViewModel_Factory implements Factory<RuleDetailViewModel> {
  private final Provider<RuleDao> ruleDaoProvider;

  private final Provider<DraftManager> draftManagerProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public RuleDetailViewModel_Factory(Provider<RuleDao> ruleDaoProvider,
      Provider<DraftManager> draftManagerProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.ruleDaoProvider = ruleDaoProvider;
    this.draftManagerProvider = draftManagerProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public RuleDetailViewModel get() {
    return newInstance(ruleDaoProvider.get(), draftManagerProvider.get(), savedStateHandleProvider.get());
  }

  public static RuleDetailViewModel_Factory create(Provider<RuleDao> ruleDaoProvider,
      Provider<DraftManager> draftManagerProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new RuleDetailViewModel_Factory(ruleDaoProvider, draftManagerProvider, savedStateHandleProvider);
  }

  public static RuleDetailViewModel newInstance(RuleDao ruleDao, DraftManager draftManager,
      SavedStateHandle savedStateHandle) {
    return new RuleDetailViewModel(ruleDao, draftManager, savedStateHandle);
  }
}
