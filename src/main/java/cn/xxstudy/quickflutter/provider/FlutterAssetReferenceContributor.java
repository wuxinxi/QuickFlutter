package cn.xxstudy.quickflutter.provider;

import com.intellij.patterns.ElementPattern;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @date: 2022/7/31 13:23
 * @author: Sensi
 * @remark:
 */
public abstract class FlutterAssetReferenceContributor extends PsiReferenceContributor {

    public abstract Class<? extends PsiElement> provideAssetStringLiteralClass();

    public abstract ElementPattern<? extends PsiElement> provideElementPatterns();

    public abstract FlutterAssetReference createAssetReference(PsiElement element);

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(provideElementPatterns(), new PsiReferenceProvider() {
            @Override
            public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                FlutterAssetReference reference = createAssetReference(element);
                if (reference == null) {
                    return PsiReference.EMPTY_ARRAY;
                }
                return new PsiReference[]{reference};
            }
        });
    }
}
