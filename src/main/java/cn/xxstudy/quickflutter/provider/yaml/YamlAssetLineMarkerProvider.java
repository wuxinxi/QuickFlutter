package cn.xxstudy.quickflutter.provider.yaml;

import cn.xxstudy.quickflutter.utils.Utils;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.ProjectScope;
import com.intellij.ui.scale.ScaleContext;
import com.intellij.util.IconUtil;
import com.intellij.util.SVGLoader;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Collection;

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
            VirtualFile virtualFile = Utils.getVirtualFile(element);
            if (Utils.isSvg(assetsPath)) {
                try {
                    icon = virtualFile != null
                            ? new ImageIcon(SVGLoader.load(null, virtualFile.getInputStream(), ScaleContext.createIdentity(), 16.0, 16.0))
                            : AllIcons.General.LayoutPreviewOnly;
                } catch (Exception e) {
                    icon = AllIcons.General.LayoutPreviewOnly;
                }
            } else {
                try {
                    icon = virtualFile != null
                            ? loadImageIcon(virtualFile)
                            : AllIcons.General.LayoutPreviewOnly;
                } catch (Exception e) {
                    icon = AllIcons.General.LayoutPreviewOnly;
                }
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

    public static Icon loadImageIcon(VirtualFile virtualFile) throws Exception {
        try (InputStream inputStream = virtualFile.getInputStream()) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage != null) {
                Image scaledImage = bufferedImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } else {
                return AllIcons.General.LayoutPreviewOnly;
            }
        }
    }


}
