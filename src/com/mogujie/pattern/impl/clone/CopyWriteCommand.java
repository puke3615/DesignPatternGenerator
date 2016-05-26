package com.mogujie.pattern.impl.clone;

import a.g.E;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiPackageImplementationHelper;
import com.mogujie.pattern.base.BaseWriteCommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<PsiClassType> list = new ArrayList<>();
        addInterfaces(mCls, list);
        boolean flag = false;
        for (PsiClassType classType : list) {
            if (CLASS_NAME_SERIALIZABLE.equals(classType.getCanonicalText())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            PsiReferenceList referenceList = mCls.getImplementsList();
            T("未实现序列化接口");
//            mCls.
            return;
        }

        try {
            generateImport();
            generateCopyMethod();
        } catch (Exception e) {
            T(e.toString());
        }
    }

    private  void addInterfaces(PsiClass cls, List<PsiClassType> list) {
        if (cls == null || list == null) {
            return;
        }
        list.addAll(Arrays.asList(cls.getImplementsListTypes()));
        addInterfaces(cls.getSuperClass(), list);
    }

    private void generateImport() {
        readyWriteImport().add(
                ByteArrayOutputStream.class,
                ObjectOutputStream.class,
                ByteArrayInputStream.class,
                ObjectInputStream.class);

    }

    private void generateCopyMethod() {
//        PsiMethod method = mFactory.createMethod(METHOD_NAME_COPY, getTypeByPsiClass(mCls));
        String className = mCls.getName();
        PsiMethod method = mFactory.createMethodFromText(String.format("public %s %s () throws Exception", className, METHOD_NAME_COPY), mCls);
//        method.getModifierList().setModifierProperty("public", true);

        PsiCodeBlock codeBlock = mFactory.createCodeBlock();
        readyWriteCode(codeBlock)
                .add(className + " obj = null;")
                .add("ByteArrayOutputStream baos = new ByteArrayOutputStream();")
                .add("ObjectOutputStream oos = new ObjectOutputStream(baos);")
                .add("oos.writeObject(this);")
                .add("ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());")
                .add("ObjectInputStream ois = new ObjectInputStream(bais);")
                .add("obj = (" + className + ") ois.readObject();")
                .add("return obj;");
        method.add(codeBlock);
        mCls.add(method);
    }
}
