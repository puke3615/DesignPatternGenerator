package com.mogujie.pattern.base;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * @author jiao
 * @version 16/5/13
 * @Mark
 */
public abstract class BaseWriteCommand extends WriteCommandAction.Simple {

    protected final Project mProject;
    protected final PsiClass mCls;
    protected final PsiFile mFile;
    protected final PsiElementFactory mFactory;

    public BaseWriteCommand(Project project, PsiClass cls, PsiFile file) {
        super(project);
        this.mProject = project;
        this.mCls = cls;
        this.mFile = file;
        this.mFactory = JavaPsiFacade.getElementFactory(project);
    }

    @Override
    protected final void run() throws Throwable {
        action();
        JavaCodeStyleManager manager = JavaCodeStyleManager.getInstance(mProject);
        manager.optimizeImports(mFile);
        manager.shortenClassReferences(mCls);
        new ReformatCodeProcessor(mProject, mCls.getContainingFile(), null, false).runWithoutProgress();
    }

    protected abstract void action();

    protected PsiType getTypeByPsiClass(PsiClass cls) {
        return getTypeByName(cls.getQualifiedName());
    }

    protected PsiType getTypeByName(String name) {
        return PsiType.getTypeByName(name, mProject, GlobalSearchScope.fileScope(mFile));
    }

    protected PsiClass getPsiClass(String className) {
        return getPsiClass(mProject, className);
    }

    protected PsiClass getPsiClass(Class cls) {
        return getPsiClass(mProject, cls.getName());
    }

    private static PsiClass getPsiClass(Project project, String className) {
        return JavaPsiFacade.getInstance(project).findClass(className, GlobalSearchScope.allScope(project));
    }

    protected WriteCodeBlockHelper readyWriteCode(PsiCodeBlock codeBlock) {
        if (codeBlock == null) {
            throw new RuntimeException("the codeBlock is null");
        }
        return new WriteCodeBlockHelper(codeBlock);
    }

    public WriterImportHelper readyWriteImport() {
        return new WriterImportHelper();
    }

    public class WriterImportHelper {
        public WriterImportHelper add(Class... clss) {
            if (mFile instanceof PsiJavaFile) {
                PsiJavaFile javaFile = (PsiJavaFile) mFile;
                for (Class cls : clss) {
                    PsiClass psiClass = getPsiClass(cls);
                    javaFile.importClass(psiClass);
                }
            }
            return this;
        }
    }

    public class WriteCodeBlockHelper {
        PsiCodeBlock codeBlock;

        private WriteCodeBlockHelper(PsiCodeBlock codeBlock) {
            this.codeBlock = codeBlock;
        }

        public WriteCodeBlockHelper add(String code) {
            return add(code, null);
        }

        public WriteCodeBlockHelper add(String code, String comment) {
            PsiStatement statement = mFactory.createStatementFromText(code, codeBlock);
            if (comment != null) {
                PsiComment commentElement = mFactory.createCommentFromText(comment, statement);
                statement.add(commentElement);
            }
            codeBlock.add(statement);
            return this;
        }

    }

    protected static boolean isEmpty(Object... ss) {
        return Util.isEmpty(ss);
    }

    protected static void T(String format, Object... params) {
        Util.show(format, params);
    }
}