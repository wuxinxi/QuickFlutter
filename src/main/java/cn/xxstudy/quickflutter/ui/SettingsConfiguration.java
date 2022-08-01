package cn.xxstudy.quickflutter.ui;
import cn.xxstudy.quickflutter.data.StorageData;
import cn.xxstudy.quickflutter.services.StorageService;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;
/**
 * @date: 2022/7/31 00:11
 * @author: Sensi
 * @remark:
 */
public class SettingsConfiguration implements SearchableConfigurable {

    private JPanel rootPanel;
    private JTextField user_name;
    private JTextField main_app_name;

    private final Project project;

    public SettingsConfiguration(Project project) {
        this.project = project;
    }

    @Override
    public @NotNull @NonNls String getId() {
        return getClass().getName();
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "QuickFlutter";
    }

    @Override
    public @Nullable JComponent createComponent() {
        user_name.setText(getStorageData().userName);
        main_app_name.setText(getStorageData().mainAppName);
        return rootPanel;
    }

    private StorageData getStorageData() {
        StorageService storageService = StorageService.getInstance(project);
        return storageService.getState();
    }

    @Override
    public boolean isModified() {
        StorageData data = getStorageData();
        return !Objects.equals(data.userName, user_name.getText().trim());
    }

    @Override
    public void apply() throws ConfigurationException {
        StorageData data = getStorageData();
        data.userName = user_name.getText().trim();
        data.mainAppName = main_app_name.getText().trim();
    }
}
