package cn.xxstudy.quickflutter.provider.dart;

import cn.xxstudy.quickflutter.services.StorageService;
import cn.xxstudy.quickflutter.utils.Utils;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.ProjectScope;
import com.intellij.ui.scale.ScaleContext;
import com.intellij.util.IconUtil;
import com.intellij.util.SVGLoader;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.psi.impl.DartReferenceExpressionImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;

/**
 * @date: 2022/7/31 16:57
 * @author: Sensi
 * @remark:
 */
public class DartAssetLineMarkerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        if (!isAssetElement(element)) {
            return;
        }
        String dartText = element.getText().replaceAll("[\"']", "");
        int index = dartText.lastIndexOf('/');
        String fileName = dartText;
        if (index != -1) {
            fileName = dartText.substring(index + 1);
        }
        boolean hasSuffix = fileName.lastIndexOf('.') != -1;

        if (element instanceof DartReferenceExpressionImpl && dartText.matches("^Res\\.\\w+$")) {
            fileName = dartText.replace("Res.", "");
            hasSuffix = false;
        }

        Project project = element.getProject();
        PsiFile[] psiFiles;
        if (hasSuffix) {
            psiFiles = FilenameIndex.getFilesByName(project, fileName, ProjectScope.getProjectScope(project));
        } else {
            psiFiles = Utils.getAssetFileWithoutSuffix(project, fileName);
        }
        if (psiFiles.length == 0) {
            return;
        }
        try {
            Icon icon;
            VirtualFile virtualFile = Utils.getVirtualFile(element);
            if (Utils.isSvg(dartText)) {
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
                            ? IconUtil.getIcon(virtualFile, Iconable.ICON_FLAG_VISIBILITY, element.getProject())
                            : AllIcons.General.LayoutPreviewOnly;
                } catch (Exception e) {
                    icon = AllIcons.General.LayoutPreviewOnly;
                }
            }
            NavigationGutterIconBuilder<PsiElement> builder =
                    NavigationGutterIconBuilder.create(icon)
                            .setTargets(psiFiles)
                            .setTooltipText("View image " + dartText);

            result.add(builder.createLineMarkerInfo(element));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isAssetElement(PsiElement element) {
        String text = element.getText();
        if (element instanceof DartReferenceExpressionImpl && text.matches("^Res\\.\\w+$")) {
            // 支持 Res.xxx 显示 gutter icon
            // NOTE 2021/8/17: 这个特性支持，违反了运行时警告：Performance warning: LineMarker is supposed to be registered for leaf elements only
            return true;
        }
        // to fix runtime warning: Performance warning: LineMarker is supposed to be registered for leaf elements only
        return element instanceof LeafPsiElement
                && ((LeafPsiElement) element).getElementType() == DartTokenTypes.REGULAR_STRING_PART
                && text.matches(DartAssetReferenceContributor.ASSET_PATTERN);
    }
}
