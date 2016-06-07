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

import java.lang.reflect.ParameterizedType;

/**
 * @author jiao
 * @version 16/5/13
 * @Mark
 */
public class BaseAction<T extends BaseWriteCommand> extends BaseGenerateAction {

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
    public void actionPerformed(AnActionEvent event) {
        mProject = event.getData(PlatformDataKeys.PROJECT);
        mEditor = event.getData(PlatformDataKeys.EDITOR);
        if (mProject == null || mEditor == null || (mFile = PsiUtilBase.getPsiFileInEditor(mEditor, mProject)) == null) {
            return;
        }
        mCls = getTargetClass(mEditor, mFile);
        BaseActionRunnable actionRunnable = null;
        try {
            Class commandCls = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            actionRunnable = (BaseActionRunnable) commandCls.getConstructor(Project.class, PsiClass.class, PsiFile.class).newInstance(mProject, mCls, mFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (actionRunnable == null) {
            actionRunnable = getCommand(mProject, mCls, mFile);
        }
        if (actionRunnable != null) {
            actionRunnable.execute();
        }
    }

    protected BaseActionRunnable getCommand(Project project, PsiClass cls, PsiFile file) {
        return null;
    }

}
