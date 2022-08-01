package cn.xxstudy.quickflutter.provider.yaml;

import cn.xxstudy.quickflutter.provider.FlutterAssetReference;
import cn.xxstudy.quickflutter.provider.FlutterAssetReferenceContributor;
import cn.xxstudy.quickflutter.utils.Utils;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

/**
 * @date: 2022/7/31 13:23
 * @author: Sensi
 * @remark:
 */
public class YamlAssetReferenceContributor extends FlutterAssetReferenceContributor {
    @Override
    public Class<? extends PsiElement> provideAssetStringLiteralClass() {
        return YAMLPlainTextImpl.class;
    }

    @Override
    public ElementPattern<? extends PsiElement> provideElementPatterns() {
        return PlatformPatterns.psiElement(provideAssetStringLiteralClass())
                .withText(StandardPatterns.string().matches(Utils.ASSET_PATTERN));
    }

    @Override
    public FlutterAssetReference createAssetReference(PsiElement element) {
        return new FlutterAssetReference(element);
    }
}
