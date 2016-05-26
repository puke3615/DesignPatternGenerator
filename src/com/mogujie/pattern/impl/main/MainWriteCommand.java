package com.mogujie.pattern.impl.main;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameterList;
import com.mogujie.pattern.base.BaseWriteCommand;

/**
 * @author zijiao
 * @version 16/5/26
 * @Mark
 */
public class MainWriteCommand extends BaseWriteCommand {

    public MainWriteCommand(Project project, PsiClass cls, PsiFile file) {
        super(project, cls, file);
    }

    @Override
    protected void action() {
        if (alreadyHasMainMehtod()) {
            T("已经包含Main方法");
            return;
        }
        generateMainMethod();
    }

    private boolean alreadyHasMainMehtod() {
        for (PsiMethod method : mCls.getMethods()) {
            PsiParameterList parameterList;
            if (method.hasModifierProperty("static")
                    && method.hasModifierProperty("public")
                    && (parameterList = method.getParameterList()).getParametersCount() > 0
                    && parameterList.getParameters()[0].getType() == getTypeByName(String[].class.getName())) {
                return true;
            }
        }
        return false;
    }

    private void generateMainMethod() {
        PsiMethod mainMethod = mFactory.createMethodFromText("public static void main(String[] args){\n\n}", mCls);
        mCls.add(mainMethod);
    }
}
