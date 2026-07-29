package com.dip.selfprotocol.di;

import com.dip.selfprotocol.data.local.SelfProtocolDatabase;
import com.dip.selfprotocol.data.local.dao.RuleDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideRuleDaoFactory implements Factory<RuleDao> {
  private final Provider<SelfProtocolDatabase> dbProvider;

  public DatabaseModule_ProvideRuleDaoFactory(Provider<SelfProtocolDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RuleDao get() {
    return provideRuleDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideRuleDaoFactory create(
      Provider<SelfProtocolDatabase> dbProvider) {
    return new DatabaseModule_ProvideRuleDaoFactory(dbProvider);
  }

  public static RuleDao provideRuleDao(SelfProtocolDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRuleDao(db));
  }
}
