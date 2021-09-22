package com.neverwell.markbook.window;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.neverwell.markbook.data.DataCenter;
import com.neverwell.markbook.data.NoteData;
import com.neverwell.markbook.dialog.NoteDialogComplex;
import com.neverwell.markbook.processor.DefaultSourceNoteData;
import com.neverwell.markbook.processor.MDFreeMarkProcessor;
import com.neverwell.markbook.processor.Processor;
import freemarker.template.TemplateException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * @author neverwell
 * @Description:
 */
public class NoteListWindow {

    private JTextField topic;
    private JTable table;
    private JButton createButton;
    private JButton clearButton;
    private JButton closeButton;
    private JPanel contentPanel;


    private JPopupMenu popupMenu;
    private Project project;

    public NoteListWindow(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.project = project;
        init();
        createButton.addActionListener(e -> {
            String topicStr = topic.getText();
            String fileName = topicStr + ".md";
            if (topicStr == null || "".equals(topicStr)) {
                topic.requestFocus();
                JOptionPane.showMessageDialog(null, "Please add the document title first!", "tips", JOptionPane.INFORMATION_MESSAGE);
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
                    NotificationGroup notificationGroup = new NotificationGroup("markbook_id", NotificationDisplayType.BALLOON, true);
                    Notification notification = notificationGroup.createNotification("Generate Document Success：" + fileFullPath, MessageType.INFO);
                    Notifications.Bus.notify(notification);
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
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);//从而获得双击时位于的单元格
                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                table.setRowSelectionInterval(row, row);
                System.out.println(e.getButton());
                if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    //通过点击位置找到点击为表格中的行
                    createPopupMenu(row);
                    //弹出菜单
                    popupMenu.show(table, e.getX(), e.getY());
                    return;
                }
                if (e.getClickCount() == 2) {
                    NoteData noteData = DataCenter.NOTE_LIST.get(row);
                    NoteDialogComplex dialog = new NoteDialogComplex(project,row, noteData);
                    dialog.setVisible(true);
                }
            }
        });
    }

    //创建右键得弹出菜单
    private void createPopupMenu(int row) {
        popupMenu = new JPopupMenu();
        JMenuItem delMenItem = new JMenuItem();
        ImageIcon logo = new ImageIcon(getClass().getResource("/img/delete.png"));
        delMenItem.setIcon(logo);
        delMenItem.setText("delete");
        delMenItem.addActionListener(evt -> {
            DataCenter.delete(row);
        });
        popupMenu.add(delMenItem);
    }

    private void init() {
        table.setModel(DataCenter.TABLE_MODEL);
        table.setEnabled(true);
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
}
