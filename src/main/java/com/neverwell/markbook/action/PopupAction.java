package com.neverwell.markbook.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.neverwell.markbook.data.DataCenter;
import com.neverwell.markbook.dialog.NoteDialog;

public class PopupAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        DataCenter.SELECT_TEXT = selectedText;
        String name = e.getRequiredData(CommonDataKeys.PSI_FILE).getViewProvider().getVirtualFile().getName();
        DataCenter.FILE_NAME = name;
        //NoteDialog dialog = new NoteDialog(project);
        //dialog.open();
        NoteDialog dialog = new NoteDialog();
        dialog.show();

    }
}
