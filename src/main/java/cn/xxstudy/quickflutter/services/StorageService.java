package cn.xxstudy.quickflutter.services;

import cn.xxstudy.quickflutter.data.StorageData;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @date: 2022/7/31 00:10
 * @author: Sensi
 * @remark:
 */
@State(name = "QuickFlutter", storages = {@Storage("quickFlutter.xml")})
public class StorageService implements PersistentStateComponent<StorageData> {
    private StorageData data = new StorageData();

    public static StorageService getInstance(Project project) {
        return project.getService(StorageService.class);
    }

    @Override
    public @NotNull StorageData getState() {
        return data;
    }

    @Override
    public void loadState(@NotNull StorageData state) {
        this.data = state;
    }
}
