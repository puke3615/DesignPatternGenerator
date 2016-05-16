package com.mogujie.pattern.impl.singleton.holder;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.mogujie.pattern.impl.singleton.BaseSingletonWriteCommand;

/**
 * @author zijiao
 * @version 16/5/14
 * @Mark
 */
public class SingletonWriteCommandByHolder extends BaseSingletonWriteCommand {

    private static final String FIELD_NAME_INSTANCE = "sInstance";
    private static final String CLASS_NAME_SUFFIX = "Holder";

    public SingletonWriteCommandByHolder(Project project, PsiClass cls, PsiFile file) {
        super(project, cls, file);
    }

    @Override
    protected void generate() {
        if (mConstructorParamCount > 0) {
            T("Holder方式不支持构造方法含有参数");
            return;
        }
        generateInnerHolderClass();
        generateStaticMethod();
    }

    private void generateInnerHolderClass() {
        PsiClass cls = mFactory.createClass(getHolderClassName());
        PsiModifierList modifierList = cls.getModifierList();
        if (modifierList == null) {
            return;
        }
        modifierList.setModifierProperty("private", true);
        modifierList.setModifierProperty("static", true);
        modifierList.setModifierProperty("final", true);

        String classNmae = mCls.getName();
        String fieldStr = String.format("static final %s %s=new %s();", classNmae, FIELD_NAME_INSTANCE, classNmae);
        PsiField field = mFactory.createFieldFromText(fieldStr,mCls);
        cls.add(field);

        mCls.add(cls);
    }

    private void generateStaticMethod() {
        PsiMethod method = getStaticMethod(false);
        PsiCodeBlock codeBlock = method.getBody();
        if (codeBlock == null) {
            return;
        }
        PsiStatement statement = mFactory.createStatementFromText(String.format("return %s.%s;", getHolderClassName(), FIELD_NAME_INSTANCE), mCls);
        codeBlock.add(statement);

        mCls.add(method);
    }

    private String getHolderClassName() {
        return String.format("%s%s", mCls.getName(), CLASS_NAME_SUFFIX);
    }
}
