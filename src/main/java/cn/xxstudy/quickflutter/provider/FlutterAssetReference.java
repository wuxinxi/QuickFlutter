package cn.xxstudy.quickflutter.provider;

import cn.xxstudy.quickflutter.utils.Utils;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.ArrayUtilRt;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.lang.dart.psi.impl.DartStringLiteralExpressionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

/**
 * @date: 2022/7/31 13:27
 * @author: Sensi
 * @remark:
 */
public class FlutterAssetReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {

    private String fileName;

    public FlutterAssetReference(@NotNull PsiElement element) {
        super(element);
        String assetsPath = element.getText();
        try {
            fileName = assetsPath.substring(assetsPath.lastIndexOf('/') + 1)
                    .replaceAll("[\"']", "");
        } catch (Exception e) {
            e.printStackTrace();
            fileName = "";
        }
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        if (!incompleteCode && !fileName.isEmpty()
                && (myElement instanceof YAMLPlainTextImpl || myElement instanceof DartStringLiteralExpressionImpl)) {
            PsiFile[] psiFiles = Utils.getAssetPsiFiles(myElement);
            ResolveResult[] results = new ResolveResult[psiFiles.length];
            for (int i = 0; i < psiFiles.length; i++) {
                results[i] = new FlutterAssetResolveResult(psiFiles[i]);
            }
            return results;
        }
        return new ResolveResult[0];
    }

    @Override
    public @Nullable PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);
        return results.length > 0 ? results[0].getElement() : null;
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        if (!(element instanceof PsiBinaryFile)) {
            return false;
        }
        String pattern = "^" + fileName.toLowerCase() + "$";
        if (!fileName.contains(".")) {
            pattern = "^" + fileName.toLowerCase() + "(.(jp(e)?g|(9.)?png|webp|bmp|svg))?$";
        }
        return ((PsiBinaryFile) element).getVirtualFile().getName().toLowerCase().matches(pattern);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        String newContent = newElementName;
        // 不管是dart还是yaml中，psiElement指向的都是asset文件，这个接收到的 newElementName 就是重命名以后的文件名，
        // 如：icon_new.png。dart和yaml中的psiElement对应的文本有两种情况：
        // 1、包含 assets/... 路径前缀，所以只需要更新不包含路径前缀的文本部分；
        // 2、只有不包含后缀的文件名部分，如：'ic_launcher_news2'
        String oldText = myElement.getText();
        int startIndex = oldText.lastIndexOf('/') + 1;
        if (!oldText.contains("/") && (oldText.startsWith("'") || oldText.startsWith("\""))) {
            startIndex++;
        }
        int endIndex = oldText.length();
        if (oldText.endsWith("'") || oldText.endsWith("\"")) {
            endIndex--;
        }
        // 当前的引用字符串无后缀
        if (!fileName.contains(".")) {
            newContent = newElementName.substring(0, newElementName.lastIndexOf('.'));
        }
        TextRange rangeInElement = new TextRange(startIndex, endIndex);
        fileName = newContent;
        return ElementManipulators.getManipulator(myElement).handleContentChange(myElement, rangeInElement, newContent);

    }

    @Override
    public @NotNull Object[] getVariants() {
        return ArrayUtilRt.EMPTY_OBJECT_ARRAY;
    }

    private static class FlutterAssetResolveResult implements ResolveResult {

        private final PsiElement psiElement;

        public FlutterAssetResolveResult(PsiElement psiElement) {
            this.psiElement = psiElement;
        }

        @Override
        public @Nullable PsiElement getElement() {
            return psiElement;
        }

        @Override
        public boolean isValidResult() {
            return psiElement instanceof PsiFile;
        }
    }
}
