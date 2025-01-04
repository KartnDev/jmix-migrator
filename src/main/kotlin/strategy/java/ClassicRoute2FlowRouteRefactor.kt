package io.kartondev.strategy.java

import io.kartondev.utils.isRootRoute
import io.kartondev.utils.isRootRouteAnnotation
import spoon.reflect.code.CtExpression
import spoon.reflect.declaration.CtAnnotation
import spoon.reflect.declaration.CtType

class ClassicRoute2FlowRouteRefactor : JavaRefactor {

    override fun canRefactor(type: CtType<*>): Boolean {
        return type.annotations.any { it.annotationType.qualifiedName.equals("io.jmix.ui.navigation.Route") }
    }

    override fun makeRefactorOnTarget(type: CtType<*>) {
        val annotation = type.annotations.filter { it.name.equals("Route") }.get(index = 0)
        if(annotation.isRootRoute()) {
            removeParameterFromAnnotation(annotation, "root")
            removeParameterFromAnnotation(annotation, "path")
            setAnnotationParameter(annotation, "value", "\"\"")
        }
    }

    @Suppress("UNUSED_PARAMETER", "SameParameterValue")
    private fun <T : Annotation> setAnnotationParameter(
        annotation: CtAnnotation<T>,
        parameterName: String,
        parameterValue: String
    ) {
        annotation.addValue<CtAnnotation<T>>("value", parameterValue)
    }

    private fun removeParameterFromAnnotation(annotation: CtAnnotation<out Annotation>, parameterName: String) {
        annotation.values.remove(parameterName)
    }


}