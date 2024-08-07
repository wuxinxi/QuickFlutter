package cn.xxstudy.quickflutter.utils;

import cn.xxstudy.quickflutter.services.StorageService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.ProjectScope;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
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

    public static VirtualFile[] getAssetVirtualFile(PsiFile[] psiFiles) {
        if (psiFiles.length == 0) {
            return new VirtualFile[]{};
        }
        VirtualFile[] virtualFiles = new VirtualFile[psiFiles.length];
        for (int i = 0; i < psiFiles.length; i++) {
            virtualFiles[i] = psiFiles[i].getVirtualFile();
        }
        return virtualFiles;
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
        files = FilenameIndex.getFilesByName(project, nameWithoutSuffix + ".svg", ProjectScope.getProjectScope(project));
        if (files.length > 0) {
            return files;
        }
        return new PsiFile[0];
    }

    public static @Nullable VirtualFile getVirtualFile(PsiElement element) {
        try {
            final String filePath = getPath(element);
            String path;
            if (!new File(filePath).exists()) {
                String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
                path = filePath.replace(fileName, "3.0x/" + fileName);
                if (!new File(path).exists()) {
                    path = filePath.replace(fileName, "2.0x/" + fileName);
                    if (!new File(path).exists()) {
                        return null;
                    }
                }
            } else {
                path = filePath;
            }
            return LocalFileSystem.getInstance().findFileByPath(path);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getPath(PsiElement element) {
        String mainAppName = StorageService.getInstance(element.getProject()).getState().mainAppName;
        if (mainAppName == null || mainAppName.isEmpty()) {
            return element.getProject().getBasePath() + "/" + element.getText();
        } else {
            return element.getProject().getBasePath() + "/" + mainAppName + "/" + element.getText();
        }
    }

    public static void writeLog(String content) {
//        if (true) return;
        try {
//            String logPath=System.getProperty("user.dir");
            String logPath = "/Users/sensiwu/Desktop";
            File file = new File(logPath + "/log2.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(content);
            fileWriter.write("\n");
            fileWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String underscoreToCamel(String underscore) {
        String[] words = underscore.split("_");
        StringBuilder camel = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            camel.append(Character.toUpperCase(words[i].charAt(0)));
            if (words[i].length() > 1) {
                camel.append(words[i].substring(1));
            }
        }
        return camel.toString();
    }
}
