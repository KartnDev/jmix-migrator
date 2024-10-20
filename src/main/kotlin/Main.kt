package io.kartondev

import spoon.Launcher
import spoon.reflect.visitor.filter.ReferenceTypeFilter
import spoon.support.compiler.FileSystemFolder
import java.io.File


fun main() {
    val launcher = Launcher()

    launcher.addInputResource(FileSystemFolder(File()))


    launcher.buildModel()


    val factory = launcher.factory


    for (ctClass in factory.Class().all) {
        if (ctClass.superclass != null && (ctClass.superclass.simpleName == "StandardLookup" || ctClass.superclass.simpleName == "StandardEditor" || ctClass.superclass.simpleName == "StandardDetailView" || ctClass.superclass.simpleName == "StandardScreen")) {

        }
    }
}