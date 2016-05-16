package com.mogujie.pattern.impl.singleton.lock;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.mogujie.pattern.model.ParamInfo;
import com.mogujie.pattern.impl.singleton.BaseSingletonWriteCommand;

/**
 * @author jiao
 * @version 16/5/13
 * @Mark
 */
public class SingletonWriteCommandByLock extends BaseSingletonWriteCommand {

    private static final String FIELD_NAME_INSTANCE = "sInstance";
    private static final String FIELD_NAME_LOCK = "sInstanceLock";

    public SingletonWriteCommandByLock(Project project, PsiClass cls, PsiFile file) {
        super(project, cls, file);
    }

    @Override
    protected void generate() {
        generateLockField();
        generateInstanceField();
        generateStaticMethod();
    }


    private void generateInstanceField() {
        PsiField instance = mFactory.createField(FIELD_NAME_INSTANCE, getTypeByPsiClass(mCls));
        instance.setInitializer(mFactory.createExpressionFromText("null", mCls));
        PsiModifierList modifierList = instance.getModifierList();
        if (modifierList != null) {
            modifierList.setModifierProperty("private", true);
            modifierList.setModifierProperty("static", true);
        }
        mCls.add(instance);
    }

    private void generateLockField() {
        PsiField lock = mFactory.createFieldFromText(String.format("private static final byte[] %s = new byte[0];", FIELD_NAME_LOCK), mCls);
        mCls.add(lock);
    }

    private void generateStaticMethod() {
        PsiMethod method = getStaticMethod(true);
        PsiCodeBlock codeBlock = method.getBody();
        if (codeBlock == null) {
            return;
        }
        String codeStr = String.format("if(%s==null){synchronized(%s){if(%s==null){%s=new %s(%s);}}}",
                FIELD_NAME_INSTANCE, FIELD_NAME_LOCK, FIELD_NAME_INSTANCE, FIELD_NAME_INSTANCE, mCls.getName(), getConstructParamStr());
        String returnThis = String.format("return %s;", FIELD_NAME_INSTANCE);
        readyWriteCode(codeBlock)
                .add(codeStr)
                .add(returnThis);
        mCls.add(method);
    }

}
