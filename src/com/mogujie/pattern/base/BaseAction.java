package com.mogujie.pattern.base;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.BaseActionRunnable;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

/**
 * @author jiao
 * @version 16/5/13
 * @Mark
 */
public abstract class BaseAction extends BaseGenerateAction {

    protected Project mProject;
    protected PsiFile mFile;
    protected Editor mEditor;
    protected PsiClass mCls;

    public BaseAction() {
        super(null);
    }

    public BaseAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        mProject = e.getData(PlatformDataKeys.PROJECT);
        mEditor = e.getData(PlatformDataKeys.EDITOR);
        if (mProject == null || mEditor == null || (mFile = PsiUtilBase.getPsiFileInEditor(mEditor, mProject)) == null) {
            return;
        }
        mCls = getTargetClass(mEditor, mFile);
        getCommand(mProject, mCls, mFile).execute();
    }

    protected abstract BaseActionRunnable getCommand(Project project, PsiClass cls, PsiFile file);

}
