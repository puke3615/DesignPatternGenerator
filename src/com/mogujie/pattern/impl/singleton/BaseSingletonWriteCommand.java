package com.mogujie.pattern.impl.singleton;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.mogujie.pattern.base.BaseWriteCommand;
import com.mogujie.pattern.model.ParamInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiao
 * @version 16/5/13
 * @Mark
 */
public abstract class BaseSingletonWriteCommand extends BaseWriteCommand {

    protected static final String METHOD_NAME_INSTANCE = "instance";
    protected final List<ParamInfo> mConstructParamInfos = new ArrayList<>();
    protected int mConstructorParamCount = 0;

    public BaseSingletonWriteCommand(Project project, PsiClass cls, PsiFile file) {
        super(project, cls, file);
    }

    @Override
    protected void action() {
        PsiMethod construct;
        PsiMethod[] methods = mCls.getConstructors();
        if (methods.length == 0) {
            construct = mFactory.createConstructor();
            construct.getModifierList().setModifierProperty("private", true);
            mCls.add(construct);
        } else if (methods.length == 1) {
            construct = methods[0];
            PsiModifierList modifierList = construct.getModifierList();
            formatModifierList(modifierList);
            for (PsiParameter parameter : construct.getParameterList().getParameters()) {
                mConstructorParamCount++;
                ParamInfo paramInfo = new ParamInfo();
                paramInfo.fullClassName = parameter.getType().getCanonicalText();
                paramInfo.name = parameter.getName();
                mConstructParamInfos.add(paramInfo);
            }
        } else {
            T("有多个构造方法");
            return;
        }

        generate();
    }

    private void formatModifierList(PsiModifierList modifierList) {
        if (modifierList.hasModifierProperty("public")) {
            modifierList.setModifierProperty("public", false);
        }
        if (modifierList.hasModifierProperty("protected")) {
            modifierList.setModifierProperty("protected", false);
        }
        modifierList.setModifierProperty("private", true);
    }

    /**
     * 返回静态方法
     *
     * @return
     */
    protected PsiMethod getStaticMethod(boolean needParam) {
        PsiMethod method = mFactory.createMethod(METHOD_NAME_INSTANCE, getTypeByPsiClass(mCls));
        method.getModifierList().setModifierProperty("public", true);
        method.getModifierList().setModifierProperty("static", true);
        if (needParam) {
            PsiParameterList parameterList = method.getParameterList();
            for (ParamInfo paramInfo : mConstructParamInfos) {
                parameterList.add(mFactory.createParameter(paramInfo.name, getTypeByName(paramInfo.fullClassName)));
            }
        }
        return method;
    }

    /**
     * 返回构造方法中的参数拼接
     *
     * @return
     */
    protected String getConstructParamStr() {
        StringBuilder paramStr = new StringBuilder();
        for (ParamInfo paramInfo : mConstructParamInfos) {
            paramStr.append(paramInfo.name).append(",");
        }
        if (paramStr.length() > 0) {
            paramStr.deleteCharAt(paramStr.length() - 1);
        }
        return paramStr.toString();
    }

    protected abstract void generate();

}
