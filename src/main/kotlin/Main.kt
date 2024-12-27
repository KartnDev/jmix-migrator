package io.kartondev

import io.kartondev.strategy.MigrationStrategy
import io.kartondev.strategy.Screen2ViewMigrationStrategy
import spoon.Launcher
import spoon.reflect.declaration.CtType
import spoon.reflect.visitor.filter.ReferenceTypeFilter
import spoon.support.compiler.FileSystemFolder
import java.io.File


fun main() {
    val launcher = Launcher()

    launcher.addInputResource(FileSystemFolder(File("/Users/cherkasov/IdeaProjects/ats-psz/ats-psz-app/src/main/java")))


    launcher.buildModel()


    val factory = launcher.factory

    val strategies = arrayOf(Screen2ViewMigrationStrategy(), Screen2ViewMigrationStrategy())

    for (ctClass: CtType<*>? in factory.Class().all) {
        for (strategy in strategies) {
            if(strategy.isSupport(ctClass!!)) {
                strategy.migrate(ctClass)
            }
        }
    }
}