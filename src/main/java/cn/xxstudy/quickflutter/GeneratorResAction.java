package cn.xxstudy.quickflutter;

import cn.xxstudy.quickflutter.services.StorageService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

import java.util.Objects;

/**
 * @date: 2022/7/31 20:48
 * @author: Sensi
 * @remark:
 */
public class GeneratorResAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        String path;
        String mainAppName = StorageService.getInstance(project).getState().mainAppName;
        if (mainAppName == null || mainAppName.isEmpty()) {
            path = project.getBasePath();
        } else {
            path = project.getBasePath() + "/" + mainAppName;
        }

    }
}
