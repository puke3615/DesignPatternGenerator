package com.mogujie.pattern.impl.singleton.lock;

import com.intellij.openapi.application.BaseActionRunnable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.mogujie.pattern.base.BaseAction;

/**
 * @author jiao
 * @version 16/5/13
 * @Mark
 */
public class SingletonByLockAction extends BaseAction {

    @Override
    protected BaseActionRunnable getCommand(Project project, PsiClass cls, PsiFile file) {
        return new SingletonWriteCommandByLock(project, cls, file);
    }
}
