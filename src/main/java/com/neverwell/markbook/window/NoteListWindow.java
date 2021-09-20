package com.neverwell.markbook.window;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.neverwell.markbook.data.DataCenter;
import com.neverwell.markbook.processor.DefaultSourceNoteData;
import com.neverwell.markbook.processor.MDFreeMarkProcessor;
import com.neverwell.markbook.processor.Processor;
import freemarker.template.TemplateException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * @author neverwell
 * @date 2021/9/17
 * @Description:
 */
public class NoteListWindow {

    private JTextField topic;
    private JTable table;
    private JButton createButton;
    private JButton clearButton;
    private JButton closeButton;
    private JPanel contentPanel;

    public NoteListWindow(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        init();
        createButton.addActionListener(e -> {
            String topicStr = topic.getText();
            String fileName = topicStr + ".md";
            if (null == topicStr || "".equals(topicStr)) {
                MessageDialogBuilder.yesNo("operating result", "title empty");
                return;
            }
            // 打开文件
            VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor(), project, project.getBaseDir());
            if (null != virtualFile) {
                String path = virtualFile.getPath();
                String fileFullPath = path + File.separator + fileName;

                Processor processor = new MDFreeMarkProcessor();
                try {
                    processor.process(new DefaultSourceNoteData(fileFullPath, topicStr, DataCenter.NOTE_LIST));
                } catch (TemplateException templateException) {
                    templateException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });
        clearButton.addActionListener(e -> DataCenter.reset());
        closeButton.addActionListener(e -> {
            toolWindow.hide(null);
        });
    }

    private void init() {
        table.setModel(DataCenter.TABLE_MODEL);
        table.setEnabled(true);
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
}
