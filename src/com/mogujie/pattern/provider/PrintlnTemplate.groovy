package com.mogujie.pattern.provider

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.codeInsight.template.impl.MacroCallNode
import com.intellij.codeInsight.template.macro.VariableOfTypeMacro;
import com.intellij.psi.PsiElement
import com.mogujie.pattern.base.Util
import com.mogujie.pattern.provider.matro.ToStringIfNeedMacro
import com.mogujie.pattern.provider.util.AndroidPostfixTemplatesUtils;
import static com.mogujie.pattern.provider.util.AndroidClassName.SYSTEM;
import org.jetbrains.annotations.NotNull;

/**
 * @author zijiao
 * @version 16/7/15
 *          Mark
 */
public class PrintlnTemplate extends AbstractRichStringBasedPostfixTemplate {


    public PrintlnTemplate() {
        this("system");
        Util.show("构造完成");
    }

    public PrintlnTemplate(int n) {
        super();
//        this("syso");
//        Util.show("构造完成");
    }

    protected PrintlnTemplate(@NotNull String alias) {
        super(alias, "System.out.println(expr);", AndroidPostfixTemplatesUtils.IS_NON_NULL);
    }

    @Override
    public String getTemplateString(@NotNull PsiElement element) {
        String ret = "";
        try {
            ret = getStaticPrefix(SYSTEM, "out", element) + '.println($expr$)$END$';
        }catch (Exception e){
            Util.show(e.message);
        }
        if(ret == null) {
            Util.show("getTemplateString $ret")
        }
        return ret;
    }

    @Override
    protected void addExprVariable(@NotNull PsiElement expr, Template template) {
        if(!expr){
            Util.show("expr")
        }
        if(!template){
            Util.show("template")
        }
        final ToStringIfNeedMacro toStringIfNeedMacro = new ToStringIfNeedMacro();
        MacroCallNode macroCallNode = new MacroCallNode(toStringIfNeedMacro);
        macroCallNode.addParameter(new ConstantNode(expr.getText()));
        template.addVariable("expr", macroCallNode, false);
    }
//
//    @Override
//    protected void setVariables(@NotNull Template template, @NotNull PsiElement element) {
//        MacroCallNode node = new MacroCallNode(new VariableOfTypeMacro());
//        node.addParameter(new ConstantNode(CONTEXT.toString()));
//        template.addVariable("context", node, new ConstantNode(""), false);
//
//    }
}
