package com.mogujie.pattern.impl;

import com.intellij.openapi.actionSystem.ex.QuickList;
import com.intellij.openapi.keymap.impl.ui.ChooseActionsDialog;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.panels.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by jiao on 16/5/13.
 */
public class Test {

    public static void main(String[] args) {

        QuickList list = new QuickList("wqeqwe", "awddasd", null);
//        new DialogBuilder()

        ChooseActionsDialog dialog = new ChooseActionsDialog(null, null, new QuickList[]{list});
//        dialog.setContentPane(contentPane);
        dialog.setModal(true);

//        dialog.add(popup);

        JBPanel panel = new JBPanel(new VerticalLayout(0));
        for (String s : getMenus()) {
            panel.add(new JLabel(s));
        }

        dialog.setSize(400, 300);
        dialog.pack();
    }

    public static java.util.List<String> getMenus() {
        return Arrays.asList("测试一下", "测试两下");
    }

}
