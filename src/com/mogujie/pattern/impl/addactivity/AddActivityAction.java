package com.mogujie.pattern.impl.addactivity;

import com.intellij.openapi.application.BaseActionRunnable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.mogujie.pattern.base.BaseAction;

/**
 * @author zijiao
 * @version 16/7/12
 *          Mark
 */
public class AddActivityAction extends BaseAction<AddActivityWriteCommand> {

    @Override
    protected BaseActionRunnable getCommand(Project project, PsiClass cls, PsiFile file) {
        return new AddActivityWriteCommand(project, cls, file);
    }
}
