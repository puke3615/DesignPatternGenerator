package com.mogujie.pattern.impl.findview;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.BaseActionRunnable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.mogujie.pattern.base.BaseAction;

/**
 * @author zijiao
 * @version 16/5/26
 * @Mark
 */
public class FindViewAction extends BaseAction {

    @Override
    protected BaseActionRunnable getCommand(Project project, PsiClass cls, PsiFile file) {
        return new FindViewWriteCommand(project, cls, file, mEditor);
    }
}
