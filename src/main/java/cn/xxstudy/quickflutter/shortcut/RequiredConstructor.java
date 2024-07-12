package cn.xxstudy.quickflutter.shortcut;

import cn.xxstudy.quickflutter.utils.Utils;
import com.intellij.codeInsight.generation.GenerateConstructorHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @date: 2023/7/19 11:47
 * @author: Sensi
 * @remark:
 */
public class RequiredConstructor extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor == null) {
            Utils.writeLog("23");
            return;
        }

        PsiFile psiFile = PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument());
        if (psiFile == null) {
            Utils.writeLog("28");
            return;
        }

        Utils.writeLog(psiFile.getName() + "," + psiFile.getText() + "," + psiFile.getProject().getName());

        // 获取 Dart 文件中的所有子元素
        PsiElement[] psiElements = psiFile.getChildren();

        Utils.writeLog(""+psiElements.length);

// 遍历所有子元素并查找 PsiClass 对象
        for (PsiElement psiElement : psiElements) {
            Utils.writeLog("42=>"+psiElement.getText()+","+psiElement.getText());
            if (psiElement instanceof PsiClass) {
                PsiClass psiClass = (PsiClass) psiElement;
                Utils.writeLog("42=>"+psiClass.getName() + "," + psiClass.getText() + "," + psiClass.toString());
            }
        }

        PsiTreeUtil.getContextOfType(psiFile,PsiClass.class);
        PsiClass[] psiClasses = PsiTreeUtil.getChildrenOfType(psiFile, PsiClass.class);
        Utils.writeLog((psiClasses == null) ? "psiClasses null" : (psiClasses.length) + "");
        for (PsiClass psiClass : psiClasses) {
            Utils.writeLog(psiClass.getName() + "," + psiClass.getText() + "," + psiClass.toString());
        }


        if (true) return;

        // 遍历所有类定义并打印类名
        PsiClass psiClass = null;
        for (PsiClass classz : psiClasses) {
            psiClass = classz;
            Utils.writeLog(psiClass.getName());
            System.out.println(psiClass.getName());
        }



//         psiClass = getSelectedPsiClass(editor, psiFile);
        if (psiClass == null) {
            Utils.writeLog("32");
            return;
        }

        if (!psiClass.isInterface() && !psiClass.isEnum() && !psiClass.hasModifierProperty(PsiModifier.ABSTRACT)) {
            GenerateConstructorHandler handler = new GenerateConstructorHandler();
            handler.invoke(editor.getProject(), editor, psiFile);

            PsiMethod[] constructors = psiClass.getConstructors();
            for (PsiMethod constructor : constructors) {
                PsiParameter[] parameters = constructor.getParameterList().getParameters();
                for (PsiParameter parameter : parameters) {
                    PsiModifierList modifierList = parameter.getModifierList();
                    if (modifierList != null) {
                        PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiFile.getProject());
                        PsiAnnotation requiredAnnotation = factory.createAnnotationFromText("required", psiClass);
                        modifierList.addBefore(requiredAnnotation, modifierList.getFirstChild());
                    }
                }
            }
        }
    }

    private PsiClass getSelectedPsiClass(Editor editor, PsiFile psiFile) {
        PsiElement psiElement = psiFile.findElementAt(editor.getCaretModel().getOffset());
        if (psiElement == null) {
            Utils.writeLog("58");
            return null;
        }

        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class, true);
        if (psiClass == null) {
            Utils.writeLog("63");
            return null;
        }

        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            Utils.writeLog("69");
            return psiClass;
        }

        PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiFile.getProject());
        PsiClass selectedClass = factory.createClassFromText(selectedText, psiElement).getContainingClass();
        if (selectedClass == null) {
            return psiClass;
        }

        return selectedClass;
    }
}
