package com.mogujie.pattern.base;

import com.intellij.openapi.editor.markup.LineMarkerRendererEx;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.progress.util.TooManyUsagesStatus;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.awt.RelativePoint;
import com.mogujie.pattern.impl.findview.NameHook;
import com.mogujie.pattern.impl.findview.ViewInfo;
import com.sun.deploy.xml.XMLAttribute;

import java.util.List;

import static com.mogujie.pattern.impl.findview.NameHook.getClassName;
import static com.mogujie.pattern.impl.findview.NameHook.getId;

/**
 * @author zijiao
 * @version 16/5/13
 * @Mark
 */
public class Util {

    public static boolean isEmpty(Object... ss) {
        for (Object s : ss) {
            if (s == null || "".equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static void show(String format, Object... params) {
        String message = isEmpty(format) ? "" : String.format(format, params);
        Messages.showMessageDialog(message, C.Message.TITLE, Messages.getInformationIcon());
    }

    public static void error(Project project, String message) {
        toast(project, MessageType.ERROR, message);
    }

    public static void info(Project project, String message) {
        toast(project, MessageType.INFO, message);
    }

    public static void warn(Project project, String message) {
        toast(project, MessageType.WARNING, message);
    }

    public static void toast(Project project, MessageType messageType, String message) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(message, messageType, null)
                .setFadeoutTime(5000)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
    }

    public static PsiFile searchLayoutFile(PsiElement element) {
        String xmlName = String.format("%s.xml", element.getText());
        return resolveLayoutFile(element, xmlName, element.getProject());
    }

    public static PsiFile resolveLayoutFile(PsiElement element, String xmlFileName, Project project) {
        Module module = ModuleUtil.findModuleForPsiElement(element);
        if (module == null) {
            return null;
        }
        GlobalSearchScope moduleScope = module.getModuleWithDependenciesAndLibrariesScope(false);
        PsiFile[] file = FilenameIndex.getFilesByName(project, xmlFileName, moduleScope);
        if (file == null || file.length <= 0) {
            file = FilenameIndex.getFilesByName(project, xmlFileName, new EverythingGlobalScope(project));
            if (file == null || file.length <= 0) {
                return null;
            }
        }
        return file[0];
    }

    public static void collectIds(final PsiFile file, final List<ViewInfo> viewInfoList) {
        file.accept(new XmlRecursiveElementVisitor() {

            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);
                if (!(element instanceof XmlTag)) {
                    return;
                }
                XmlTag tag = (XmlTag) element;
                boolean isInclude;
                if (isInclude = "include".equals(tag.getName())) {
                    XmlAttribute layoutAttrbute = tag.getAttribute("layout", null);
                    String layoutStr;
                    if (layoutAttrbute != null && (layoutStr = layoutAttrbute.getValue()) != null) {
                        String[] splits = layoutStr.split("/");
                        if (splits.length == 2) {
                            String layout = splits[1];
                            PsiFile include = resolveLayoutFile(file, String.format("%s.xml", layout), file.getProject());
                            collectIds(include, viewInfoList);
                            return;
                        }
                    }
                }

                XmlAttribute idAttribute = tag.getAttribute("android:id", null);
                if (idAttribute == null) {
                    return;
                }
                String idStr = idAttribute.getValue();
                final String idModifier = "@+id/";
                if (idStr != null && idStr.startsWith(idModifier)) {
                    String id = idStr.substring(idModifier.length());
                    if ("".equals(id)) {
                        return;
                    }
                    String className = isInclude ? "android.view.View" : tag.getName();

                    //do hook operation
                    className = getClassName(className);
                    id = getId(id);

                    viewInfoList.add(new ViewInfo(className, id));
                }

            }
        });
    }

    private static String formatViewName(String className) {
        return className;
    }
}
