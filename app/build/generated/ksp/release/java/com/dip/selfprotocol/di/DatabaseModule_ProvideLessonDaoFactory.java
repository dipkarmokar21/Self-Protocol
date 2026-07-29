package com.dip.selfprotocol.di;

import com.dip.selfprotocol.data.local.SelfProtocolDatabase;
import com.dip.selfprotocol.data.local.dao.LessonDao;
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
public final class DatabaseModule_ProvideLessonDaoFactory implements Factory<LessonDao> {
  private final Provider<SelfProtocolDatabase> dbProvider;

  public DatabaseModule_ProvideLessonDaoFactory(Provider<SelfProtocolDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public LessonDao get() {
    return provideLessonDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideLessonDaoFactory create(
      Provider<SelfProtocolDatabase> dbProvider) {
    return new DatabaseModule_ProvideLessonDaoFactory(dbProvider);
  }

  public static LessonDao provideLessonDao(SelfProtocolDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideLessonDao(db));
  }
}
