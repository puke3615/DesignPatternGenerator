package com.mogujie.pattern.impl.findview;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.mogujie.pattern.base.BaseWriteCommand;
import com.mogujie.pattern.base.Util;

import java.util.ArrayList;
import java.util.List;

import static com.mogujie.pattern.base.Util.show;
import static com.mogujie.pattern.impl.findview.NameHook.getFieldName;

/**
 * @author zijiao
 * @version 16/5/26
 * @Mark
 */
public class FindViewWriteCommand extends BaseWriteCommand {

    private static final String FIND_VIEW_METHOD_NAME = "findView";
    private final Editor mEditor;
    private final List<ViewInfo> mViewInfoList = new ArrayList<>();

    public FindViewWriteCommand(Project project, PsiClass cls, PsiFile file, Editor editor) {
        super(project, cls, file);
        this.mEditor = editor;
    }

    @Override
    protected void action() {
        PsiFile layout = findLayout(0);
        if (layout == null) {
            error("无法找到Layout");
            return;
        }

        Util.collectIds(layout, mViewInfoList);
        if (mViewInfoList.size() == 0) {
            warn("未发现Id");
            return;
        }
        generateCode();
    }

    protected void generateCode() {
        generateFindViewMethod();
        callFindViewMethod();
    }

    private void generateFindViewMethod() {
        PsiMethod findView = mFactory.createMethod(FIND_VIEW_METHOD_NAME, PsiType.VOID);
        findView.getModifierList().setModifierProperty("private", true);
        PsiCodeBlock codeBlock = findView.getBody();
        if (codeBlock == null) {
            return;
        }
        for (ViewInfo viewInfo : mViewInfoList) {
            String fieldName = getFieldName(viewInfo.id);

            PsiField field = mFactory.createField(fieldName, getTypeByName(viewInfo.className));
            PsiModifierList ml = field.getModifierList();
            if (ml != null) {
                ml.setModifierProperty("private", true);
            }
            mCls.add(field);

            String castName = "android.view.View".equals(viewInfo.className) ? "" : String.format("(%s)", viewInfo.className);
            StringBuilder codeLine = new StringBuilder()
                    .append(fieldName)
                    .append("=")
                    .append(castName)
                    .append("findViewById(R.id.")
                    .append(viewInfo.id)
                    .append(");");
            PsiStatement statement = mFactory.createStatementFromText(codeLine.toString(), mCls);
            codeBlock.add(statement);
        }
        mCls.add(findView);
    }

    private void callFindViewMethod() {
        PsiElement element = mFile.findElementAt(mEditor.getCaretModel().getOffset());
        if (element == null) {
            return;
        }
        while (!(element instanceof PsiMethod)) {
            element = element.getParent();
        }
        PsiCodeBlock codeBlock = ((PsiMethod) element).getBody();
        if (codeBlock == null) {
            return;
        }
        codeBlock.add(mFactory.createStatementFromText(FIND_VIEW_METHOD_NAME + "();", mCls));
    }

    private PsiFile findLayout(int offsetDif) {
        int offset = mEditor.getCaretModel().getOffset() + offsetDif;
        PsiElement element = mFile.findElementAt(offset);
        if (!(element instanceof PsiIdentifier)) {
            return null;
        }
        PsiElement layout = element.getParent().getFirstChild();
        if (layout == null) {
            return null;
        }
        if (!"R.layout".equals(layout.getText())) {
            return null;
        }
        PsiFile file = Util.searchLayoutFile(element);
        return file == null && offsetDif == 0 ? findLayout(-1) : file;
    }


}
