package org.touchhome.bundle.mongodb.setting;

import java.nio.file.Path;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.touchhome.bundle.api.EntityContext;
import org.touchhome.bundle.api.entity.dependency.DependencyExecutableInstaller;
import org.touchhome.bundle.api.setting.SettingPluginButton;
import org.touchhome.bundle.api.setting.SettingPluginText;
import org.touchhome.bundle.api.ui.field.ProgressBar;
import org.touchhome.bundle.hquery.hardware.other.MachineHardwareRepository;

@Log4j2
@Component
public class MongoDBDependencyExecutableInstaller extends DependencyExecutableInstaller {

  @Override
  public String getName() {
    return "mongodb";
  }

  @Override
  public Path installDependencyInternal(@NotNull EntityContext entityContext, @NotNull ProgressBar progressBar) {
    if (SystemUtils.IS_OS_LINUX) {
      MachineHardwareRepository machineHardwareRepository = entityContext.getBean(MachineHardwareRepository.class);
      machineHardwareRepository.installSoftware(getName(), 600);
      machineHardwareRepository.enableAndStartSystemctl(getName());
    }
    return null;
  }

  @Override
  public synchronized boolean isRequireInstallDependencies(@NotNull EntityContext entityContext, boolean useCacheIfPossible) {
    return SystemUtils.IS_OS_LINUX && super.isRequireInstallDependencies(entityContext, useCacheIfPossible);
  }

  @Override
  public boolean checkWinDependencyInstalled(MachineHardwareRepository repository, @NotNull Path targetPath) {
    return !repository.execute(targetPath + " -version").startsWith("MongoDB shell");
  }

  @Override
  public @NotNull Class<? extends SettingPluginText> getDependencyPluginSettingClass() {
    return MongoDBPathSetting.class;
  }

  @Override
  public Class<? extends SettingPluginButton> getInstallButton() {
    return MongoDBInstallSetting.class;
  }
}
