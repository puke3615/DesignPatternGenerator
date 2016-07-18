package com.mogujie.pattern.provider;

import com.intellij.codeInsight.template.postfix.templates.JavaPostfixTemplateProvider;
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate;
import com.intellij.util.containers.ContainerUtil;
import com.mogujie.pattern.base.Util;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author zijiao
 * @version 16/7/15
 *          Mark
 */
public class AndroidPostfixTemplateProvider extends JavaPostfixTemplateProvider {

    private Set<PostfixTemplate> mTemplates;

    public AndroidPostfixTemplateProvider() {
        Util.show("hello 开始构造");
        mTemplates = ContainerUtil.<PostfixTemplate>newHashSet(
                getTe()
        );
        Util.show("hello  结束构造");
    }

    private PostfixTemplate getTe() {
        Util.show("准备构造了..");
        try {
            return new PrintlnTemplate();
        } catch (Exception e) {
            Util.show(e.getMessage());
        }
        return new PrintlnTemplate(1);
    }

    @NotNull
    @Override
    public Set<PostfixTemplate> getTemplates() {
        Util.show("hello 111111");
        return mTemplates;
    }
}
