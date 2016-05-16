package com.mogujie.pattern.base;

import com.intellij.codeInsight.generation.actions.GenerateActionPopupTemplateInjector;
import com.intellij.internal.psiView.PsiViewerDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.ui.components.panels.VerticalLayout;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author zijiao
 * @version 16/5/13
 * @Mark
 */
public class GroupAction extends AnAction {

    private static final String GENERATE_DESIGN_PATTERN = "Generate Design Pattern";
    private static final String DESIGN_PATTERN_GENERATOR = "DesignPatternGroup";

    @Override
    public void actionPerformed(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();

        Project project = ObjectUtils.assertNotNull(getEventProject(e));
        final ListPopup popup =
                JBPopupFactory.getInstance().createActionGroupPopup(
                        GENERATE_DESIGN_PATTERN,
                        wrapGroup(getGroup(), dataContext, project),
                        dataContext,
                        JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                        false);

        popup.showInBestPositionFor(dataContext);
    }

    private static DefaultActionGroup getGroup() {
        return (DefaultActionGroup)ActionManager.getInstance().getAction(DESIGN_PATTERN_GENERATOR);
    }

    private static DefaultActionGroup wrapGroup(DefaultActionGroup actionGroup, DataContext dataContext, @NotNull Project project) {
        final DefaultActionGroup copy = new DefaultActionGroup();
        for (final AnAction action : actionGroup.getChildren(null)) {
            if (DumbService.isDumb(project) && !action.isDumbAware()) {
                continue;
            }

            if (action instanceof GenerateActionPopupTemplateInjector) {
                final AnAction editTemplateAction = ((GenerateActionPopupTemplateInjector)action).createEditTemplateAction(dataContext);
                if (editTemplateAction != null) {
                    copy.add(new GenerateWrappingGroup(action, editTemplateAction));
                    continue;
                }
            }
            if (action instanceof DefaultActionGroup) {
                copy.add(wrapGroup((DefaultActionGroup)action, dataContext, project));
            }
            else {
                copy.add(action);
            }
        }
        return copy;
    }

    private static class GenerateWrappingGroup extends ActionGroup {

        private final AnAction myAction;
        private final AnAction myEditTemplateAction;

        public GenerateWrappingGroup(AnAction action, AnAction editTemplateAction) {
            myAction = action;
            myEditTemplateAction = editTemplateAction;
            copyFrom(action);
            setPopup(true);
        }

        @Override
        public boolean canBePerformed(DataContext context) {
            return true;
        }

        @NotNull
        @Override
        public AnAction[] getChildren(@Nullable AnActionEvent e) {
            return new AnAction[] {myEditTemplateAction};
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            myAction.actionPerformed(e);
        }
    }

    private void T(Object s) {
        Util.show(String.valueOf(s));
    }

}