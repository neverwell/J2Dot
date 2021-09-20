package com.neverwell.markbook.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.ui.EditorTextField;
import com.neverwell.markbook.data.DataCenter;
import com.neverwell.markbook.data.DataConvert;
import com.neverwell.markbook.data.NoteData;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author neverwell
 * @Description:
 */
public class NoteDialog extends DialogWrapper {
    private EditorTextField title;
    private EditorTextField mark;

    public NoteDialog() {
        super(true);
        setTitle("add notes");
        init();
    }

    @Override
    protected @Nullable
    JComponent createCenterPanel() {
        JPanel jPanel = new JPanel(new BorderLayout());
        title = new EditorTextField("note title");
        mark = new EditorTextField("note content");
        mark.setPreferredSize(new Dimension(200, 100));

        jPanel.add(title, BorderLayout.NORTH);
        jPanel.add(mark, BorderLayout.CENTER);
        return jPanel;

    }

    @Override
    protected JComponent createSouthPanel() {
        JPanel jPanel = new JPanel();
        JButton jButton = new JButton("add to note lists");
        jButton.addActionListener(e -> {
            String title = this.title.getText();
            String mark = this.mark.getText();
            String fileType = DataCenter.FILE_NAME.substring(DataCenter.FILE_NAME.lastIndexOf(".") + 1);
            NoteData noteData = new NoteData(title, mark, DataCenter.SELECT_TEXT, DataCenter.FILE_NAME, fileType);
            DataCenter.NOTE_LIST.add(noteData);
            DataCenter.TABLE_MODEL.addRow(DataConvert.convert(noteData));
            MessageDialogBuilder.yesNo("operating result", "create success");
            NoteDialog.this.dispose();

        });
        jPanel.add(jButton);
        return jPanel;
    }
}
