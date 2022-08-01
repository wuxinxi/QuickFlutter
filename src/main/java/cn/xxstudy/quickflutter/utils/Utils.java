package cn.xxstudy.quickflutter.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.ProjectScope;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date: 2022/7/31 00:09
 * @author: Sensi
 * @remark:
 */
public class Utils {
    public static final String ASSET_PATTERN = "^asset(s)?(/([-\\w\\u00C0-\\u017F]+|[1-9]\\.\\dx))*/([-\\w\\u00C0-\\u017F]+\\.(jp(e)?g|(9.)?png|webp|bmp|svg)?)?$";

    public static String underlineToHump(String str) {
        str = str.toLowerCase();
        Pattern compile = Pattern.compile("_[a-z]");
        Matcher matcher = compile.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(0).toUpperCase().replace("_", ""));
        }
        matcher.appendTail(sb);
        sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
        return sb.toString();
    }

    public static String createCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return format.format(new Date());
    }

    public static boolean isAssetElement(PsiElement element) {
        String text = element.getText();
        return element instanceof LeafPsiElement && text.matches(ASSET_PATTERN) && text.lastIndexOf('.') != -1;
    }

    public static boolean isSvg(String name) {
        return name.endsWith(".svg");
    }

    public static PsiFile[] getAssetPsiFiles(PsiElement element) {
        String text = element.getText().replaceAll("[\"']", "");
        String fileName = text;
        int slashIndex = text.lastIndexOf('/');
        if (slashIndex != -1) {
            fileName = text.substring(text.lastIndexOf('/') + 1);
        }
        boolean hasSuffix = fileName.lastIndexOf('.') != -1;
        Project project = element.getProject();
        if (hasSuffix) {
            return FilenameIndex.getFilesByName(project, fileName, ProjectScope.getProjectScope(project));
        }
        return getAssetFileWithoutSuffix(project, fileName);
    }

    public static PsiFile[] getAssetFileWithoutSuffix(Project project, String nameWithoutSuffix) {
        PsiFile[] files = FilenameIndex.getFilesByName(project, nameWithoutSuffix + ".png", ProjectScope.getProjectScope(project));
        if (files.length > 0) {
            return files;
        }
        files = FilenameIndex.getFilesByName(project, nameWithoutSuffix + ".jpg", ProjectScope.getProjectScope(project));
        if (files.length > 0) {
            return files;
        }
        files = FilenameIndex.getFilesByName(project, nameWithoutSuffix + ".jpeg", ProjectScope.getProjectScope(project));
        if (files.length > 0) {
            return files;
        }
        files = FilenameIndex.getFilesByName(project, nameWithoutSuffix + ".webp", ProjectScope.getProjectScope(project));
        if (files.length > 0) {
            return files;
        }
        files = FilenameIndex.getFilesByName(project, nameWithoutSuffix + ".bmp", ProjectScope.getProjectScope(project));
        if (files.length > 0) {
            return files;
        }
        return new PsiFile[0];
    }
}
