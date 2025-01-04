package io.kartondev.strategy.java

import io.kartondev.processor.annotation.AnnotationRenameProcessor
import spoon.reflect.declaration.CtType

class UiController2ViewControllerRefactor : JavaRefactor {

    override fun canRefactor(type: CtType<*>): Boolean {
        return type.annotations.any { it.annotationType.qualifiedName.equals("io.jmix.ui.screen.UiController") }
    }

    override fun makeRefactorOnTarget(type: CtType<*>) {
        val uiController = type.annotations.filter { it.name.equals("UiController") }.get(index = 0)
        AnnotationRenameProcessor<Annotation>().process(type, uiController, "io.jmix.flowui.view.ViewController")
    }
}