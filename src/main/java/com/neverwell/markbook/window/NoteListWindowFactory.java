package com.neverwell.markbook.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author neverwell
 * @Description:
 */
public class NoteListWindowFactory implements ToolWindowFactory {


    //@Override
    //public boolean isApplicable(@NotNull Project project) {
    //    return ToolWindowFactory.super.isApplicable(project);
    //}

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        NoteListWindow noteListWindow = new NoteListWindow(project, toolWindow);
        // 获取内容工厂实例
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        // 从内容工厂中获取用于 toolWindow 显示的内容
        Content content = contentFactory.createContent(noteListWindow.getContentPanel(), "", false);
        // 为 toolWindow 设置内容
        toolWindow.getContentManager().addContent(content);

    }

    @Override
    public void init(@NotNull ToolWindow toolWindow) {
        ToolWindowFactory.super.init(toolWindow);
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return ToolWindowFactory.super.shouldBeAvailable(project);
    }

    @Override
    public boolean isDoNotActivateOnStart() {
        return ToolWindowFactory.super.isDoNotActivateOnStart();
    }
}
