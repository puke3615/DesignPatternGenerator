package com.mogujie.pattern.impl.addactivity;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlTagNameProvider;
import com.mogujie.pattern.base.BaseWriteCommand;
import com.sun.xml.internal.bind.v2.util.XmlFactory;

/**
 * @author zijiao
 * @version 16/7/12
 *          Mark
 */
public class AddActivityWriteCommand extends BaseWriteCommand {
    public AddActivityWriteCommand(Project project, PsiClass cls, PsiFile file) {
        super(project, cls, file);
    }

    @Override
    protected void run() throws Throwable {
        if (!isActivity(mCls)) {
            error("该类不是Activity");
            return;
        }
        Helper.addActivity(mCls, this);
    }

    private boolean isActivity(PsiClass cls) {
        if ("java.lang.Object".equals(cls.getQualifiedName())) {
            return false;
        } else if (getTypeByName("android.app.Activity").isAssignableFrom(getTypeByPsiClass(cls))) {
            return true;
        } else {
            return isActivity(cls.getSuperClass());
        }
    }

    @Override
    protected void action() {

    }
}
