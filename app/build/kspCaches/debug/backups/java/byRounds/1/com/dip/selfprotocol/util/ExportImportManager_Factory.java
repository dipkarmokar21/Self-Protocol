package com.dip.selfprotocol.util;

import android.content.Context;
import com.dip.selfprotocol.data.local.dao.CategoryDao;
import com.dip.selfprotocol.data.local.dao.LessonDao;
import com.dip.selfprotocol.data.local.dao.RuleDao;
import com.dip.selfprotocol.domain.repository.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ExportImportManager_Factory implements Factory<ExportImportManager> {
  private final Provider<Context> contextProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  private final Provider<RuleDao> ruleDaoProvider;

  private final Provider<LessonDao> lessonDaoProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public ExportImportManager_Factory(Provider<Context> contextProvider,
      Provider<CategoryDao> categoryDaoProvider, Provider<RuleDao> ruleDaoProvider,
      Provider<LessonDao> lessonDaoProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.categoryDaoProvider = categoryDaoProvider;
    this.ruleDaoProvider = ruleDaoProvider;
    this.lessonDaoProvider = lessonDaoProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public ExportImportManager get() {
    return newInstance(contextProvider.get(), categoryDaoProvider.get(), ruleDaoProvider.get(), lessonDaoProvider.get(), settingsRepositoryProvider.get());
  }

  public static ExportImportManager_Factory create(Provider<Context> contextProvider,
      Provider<CategoryDao> categoryDaoProvider, Provider<RuleDao> ruleDaoProvider,
      Provider<LessonDao> lessonDaoProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new ExportImportManager_Factory(contextProvider, categoryDaoProvider, ruleDaoProvider, lessonDaoProvider, settingsRepositoryProvider);
  }

  public static ExportImportManager newInstance(Context context, CategoryDao categoryDao,
      RuleDao ruleDao, LessonDao lessonDao, SettingsRepository settingsRepository) {
    return new ExportImportManager(context, categoryDao, ruleDao, lessonDao, settingsRepository);
  }
}
