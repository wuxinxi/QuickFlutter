package cn.xxstudy.quickflutter.provider.yaml;

import cn.xxstudy.quickflutter.services.StorageService;
import cn.xxstudy.quickflutter.utils.Utils;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.ProjectScope;
import com.intellij.ui.scale.ScaleContext;
import com.intellij.util.SVGLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;
import java.util.Objects;

/**
 * @date: 2022/7/31 12:57
 * @author: Sensi
 * @remark: 自定义线标记提供器，左侧显示icon
 */
public class YamlAssetLineMarkerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element,
                                            @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        super.collectNavigationMarkers(element, result);
        if (!Utils.isAssetElement(element)) {
            return;
        }
        Project project = element.getProject();
        String assetsPath = element.getText();
        String fileName = assetsPath.substring(assetsPath.lastIndexOf('/') + 1).replaceAll("\"", "");
        PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, fileName, ProjectScope.getProjectScope(project));
        if (psiFiles.length == 0) {
            return;
        }
        try {
            Icon icon;
            if (Utils.isSvg(assetsPath)) {

                String filePath;
                String mainAppName = StorageService.getInstance(project).getState().mainAppName;
                if (mainAppName == null || mainAppName.isEmpty()) {
                    filePath = element.getProject().getBasePath() + "/" + element.getText();
                } else {
                    filePath = element.getProject().getBasePath() + "/" + mainAppName + "/" + element.getText();
                }
                VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(filePath);
                try {
                    icon = new ImageIcon(SVGLoader.load(null, virtualFile.getInputStream(), ScaleContext.createIdentity(), 16.0, 16.0));
                } catch (Exception e) {
                    icon = AllIcons.General.LayoutPreviewOnly;
                }
            } else {
                icon = AllIcons.General.LayoutPreviewOnly;
            }
            NavigationGutterIconBuilder<PsiElement> builder =
                    NavigationGutterIconBuilder.create(icon)
                            .setTargets(psiFiles)
                            .setTooltipText("View image " + assetsPath.replaceAll("\"", ""));

            result.add(builder.createLineMarkerInfo(element));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
