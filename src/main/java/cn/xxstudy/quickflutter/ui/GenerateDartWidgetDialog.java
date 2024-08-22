package cn.xxstudy.quickflutter.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;
/**
 * @date: 2022/7/31 00:12
 * @author: Sensi
 * @remark:
 */
public class GenerateDartWidgetDialog extends DialogWrapper {

    private final Listener listener;
    private JTextField classNameField;
    private JCheckBox createStatefulCheckBox;
    private JPanel contentPanel;

    public GenerateDartWidgetDialog(final Listener listener) {
        super((Project) null);
        this.listener = listener;
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        return contentPanel;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        this.listener.onGenerateClicked(classNameField.getText(), createStatefulCheckBox.isSelected());
    }

    @Override
    public boolean isOKActionEnabled() {
        return super.isOKActionEnabled();
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return classNameField;
    }


    public interface Listener {
        void onGenerateClicked(String className, boolean createStateful);
    }
}
