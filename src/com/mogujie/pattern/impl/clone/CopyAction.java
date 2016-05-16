package com.mogujie.pattern.impl.clone;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.BaseActionRunnable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.mogujie.pattern.base.BaseAction;

/**
 * @author zijiao
 * @version 16/5/16
 * @Mark
 */
public class CopyAction extends BaseAction {

    @Override
    protected BaseActionRunnable getCommand(Project project, PsiClass cls, PsiFile file) {
        return new CopyWriteCommand(project, cls, file);
    }
}
