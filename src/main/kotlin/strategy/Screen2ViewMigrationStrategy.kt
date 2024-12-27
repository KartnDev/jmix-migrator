package io.kartondev.strategy

import spoon.reflect.code.CtExpression
import spoon.reflect.declaration.*
import spoon.support.reflect.code.CtLiteralImpl
import utils.toRefactoredFlowUiControllerName

class Screen2ViewMigrationStrategy : MigrationStrategy {

    override fun isSupport(type: CtType<*>): Boolean {
        return type.superclass != null &&
            (type.superclass.simpleName == "StandardLookup" ||
                    type.superclass.simpleName == "StandardEditor" ||
                    type.superclass.simpleName == "StandardDetailView" ||
                    type.superclass.simpleName == "StandardScreen")
    }

    override fun migrate(type: CtType<*>) {
        val uiController = type.annotations.filter { it.name.equals("UiController") }.get(index = 0)
        renameAnnotation(type, uiController, "io.jmix.flowui.view.ViewController")

        val uiDescriptor = type.annotations.filter { it.name.equals("UiDescriptor") }.get(index = 0)
        renameAnnotation(type, uiDescriptor, "io.jmix.flowui.view.ViewDescriptor")

    }

    private fun <A: Annotation> renameAnnotation(type: CtType<*>, annotation: CtAnnotation<out A>, newAnnotationName: String) {

    }


}

