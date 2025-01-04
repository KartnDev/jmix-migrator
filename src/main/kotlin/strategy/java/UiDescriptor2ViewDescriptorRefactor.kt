package io.kartondev.strategy.java

import io.kartondev.processor.annotation.AnnotationRenameProcessor
import spoon.reflect.declaration.CtType

class UiDescriptor2ViewDescriptorRefactor : JavaRefactor {

    override fun canRefactor(type: CtType<*>): Boolean {
        return type.annotations.any { it.annotationType.qualifiedName.equals("io.jmix.ui.screen.UiDescriptor") }
    }

    override fun makeRefactorOnTarget(type: CtType<*>) {
        val uiDescriptor = type.annotations.filter { it.name.equals("UiDescriptor") }.get(index = 0)
        AnnotationRenameProcessor<Annotation>().process(type, uiDescriptor, "ViewDescriptor")
    }
}