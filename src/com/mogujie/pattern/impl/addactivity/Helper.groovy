package com.mogujie.pattern.impl.addactivity

import com.intellij.codeInsight.actions.ReformatCodeProcessor
import com.intellij.codeInsight.daemon.impl.tagTreeHighlighting.XmlTagTreeHighlightingPassFactory
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtil
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.XmlRecursiveElementVisitor
import com.intellij.psi.impl.PsiFileFactoryImpl
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.impl.source.xml.XmlTagImpl
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlTag
import com.mogujie.pattern.base.Util
import groovy.io.FileType
import groovy.io.FileVisitResult
import groovy.xml.XmlUtil

/**
 * @author zijiao
 * @version 16/7/12
 * Mark 
 */
class Helper {

    static def ANDROID_MANIFEST = 'AndroidManifest.xml'

    static void addActivity(PsiClass cls, AddActivityWriteCommand addActivityWriteCommand) {
        def module = ModuleUtil.findModuleForPsiElement(cls)
        def searchPath = module.moduleFile.parent.path + "/src"
        new File(searchPath).traverse {
            if (ANDROID_MANIFEST.equals(it.name)) {
                insertToManifest(it.absolutePath, cls.qualifiedName, addActivityWriteCommand)
                FileVisitResult.TERMINATE
            }
        }
    }

    static void insertToManifest(String manifestPath, String activityName, AddActivityWriteCommand addActivityWriteCommand) {
        def content = new File(manifestPath).text
        if (content.contains(activityName)) {
            addActivityWriteCommand.error('已经添加过该Activity')
            return
        }
        def applicationIndex = content.indexOf('<application')
        def targetIndex = content.indexOf('>', applicationIndex) + 1

        def result = new StringBuilder()
        result.append(content.substring(0, targetIndex))
        result.append("\n\t\t<activity android:name=\"${activityName}\" />")
        result.append(content.substring(targetIndex, content.length()))

        new File(manifestPath).withOutputStream {
            it.write(result.toString().bytes)
        }
        addActivityWriteCommand.info('添加完成')
    }

    static void T(String str) {
        Util.show(str)
    }

}