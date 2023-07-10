package cn.xxstudy.quickflutter.shortcut;

import cn.xxstudy.quickflutter.utils.Utils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

/**
 * @date: 2023/7/10 11:42
 * @author: Sensi
 * @remark:
 */
public class CamelcaseFormat extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        WriteCommandAction.runWriteCommandAction(e.getProject(), () -> {
            Editor data = e.getData(CommonDataKeys.EDITOR);
            if (data != null) {
                ApplicationManager.getApplication().runReadAction(() -> {
                    String selectedText = data.getSelectionModel().getSelectedText();
                    if (selectedText != null) {
                        Editor editor = e.getData(CommonDataKeys.EDITOR);
                        if (editor != null) {
                            Document document = editor.getDocument();
                            int start = editor.getSelectionModel().getSelectionStart();
                            int end = editor.getSelectionModel().getSelectionEnd();
                            String newValue = Utils.underscoreToCamel(selectedText);
                            document.replaceString(start, end, newValue);
                        }
                    }
                });

            }

        });


    }
}
