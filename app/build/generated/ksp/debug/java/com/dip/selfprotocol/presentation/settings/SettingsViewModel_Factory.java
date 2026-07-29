package com.dip.selfprotocol.presentation.settings;

import com.dip.selfprotocol.domain.repository.SettingsRepository;
import com.dip.selfprotocol.util.ExportImportManager;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<ExportImportManager> exportImportManagerProvider;

  public SettingsViewModel_Factory(Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<ExportImportManager> exportImportManagerProvider) {
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.exportImportManagerProvider = exportImportManagerProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(settingsRepositoryProvider.get(), exportImportManagerProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<ExportImportManager> exportImportManagerProvider) {
    return new SettingsViewModel_Factory(settingsRepositoryProvider, exportImportManagerProvider);
  }

  public static SettingsViewModel newInstance(SettingsRepository settingsRepository,
      ExportImportManager exportImportManager) {
    return new SettingsViewModel(settingsRepository, exportImportManager);
  }
}
