package com.mogujie.pattern.impl.clone;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.mogujie.pattern.base.BaseWriteCommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author zijiao
 * @version 16/5/16
 * @Mark
 */
public class CopyWriteCommand extends BaseWriteCommand {

    private static final String CLASS_NAME_SERIALIZABLE = "java.io.Serializable";
    private static final String METHOD_NAME_COPY = "copy";

    public CopyWriteCommand(Project project, PsiClass cls, PsiFile file) {
        super(project, cls, file);
    }

    @Override
    protected void action() {
//        if (!getTypeByName(CLASS_NAME_SERIALIZABLE).isConvertibleFrom(getTypeByPsiClass(mCls))) {
//            T("该类未实现java.io.Serializable接口");
//            return;
//        }
        generateImport();
        generateCopyMethod();
    }

    private void generateImport() {
        readyWriteImport().add(
                ByteArrayOutputStream.class,
                ObjectOutputStream.class,
                ByteArrayInputStream.class,
                ObjectInputStream.class);

    }

    private void generateCopyMethod() {
        PsiMethod method = mFactory.createMethod(METHOD_NAME_COPY, getTypeByPsiClass(mCls));
        method.getModifierList().setModifierProperty("public", true);
        PsiCodeBlock codeBlock = method.getBody();
        if (codeBlock == null) {
            return;
        }
        String className = mCls.getName();
        String code = String.format(
                "%s obj = null;" +
                        "try {" +
                        "ByteArrayOutputStream baos = new ByteArrayOutputStream();" +
                        "ObjectOutputStream oos = null;" +
                        "oos = new ObjectOutputStream(baos);" +
                        "oos.writeObject(this);\n" +
                        "ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());" +
                        "ObjectInputStream ois = new ObjectInputStream(bais);" +
                        "obj = (%s) ois.readObject();" +
                        "} catch (Exception e) {" +
                        "e.printStackTrace();}" +
                        "return obj)", className, className);
        readyWriteCode(codeBlock).add(code);
        mCls.add(method);
    }
}
