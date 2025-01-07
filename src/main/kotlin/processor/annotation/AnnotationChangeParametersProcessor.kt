package io.kartondev.processor.annotation

import io.kartondev.processor.RefactorProcessor
import spoon.reflect.code.CtExpression
import spoon.reflect.declaration.*
import spoon.reflect.factory.TypeFactory
import spoon.reflect.reference.CtTypeReference
import spoon.support.reflect.code.CtLiteralImpl
import utils.toRefactoredFlowUiControllerName

class AnnotationChangeParametersProcessor<A : Annotation> {

    fun process(
        unit: CtType<*>,
        newAnnotationName: String,
        newParameters: Map<String, MethodParams>,
        targetAnnotation: CtAnnotation<out A>?,
    ) {
        val factory = unit.factory

        @Suppress("UNCHECKED_CAST")
        val annotationType: CtAnnotationType<A> =
            factory.Annotation().create(newAnnotationName) as CtAnnotationType<A>

        newParameters.forEach {
            val newMethod: CtAnnotationMethod<CtMethod<Any>> = factory.Core().createAnnotationMethod()
            newMethod.setSimpleName<CtNamedElement>(it.value.name)
            newMethod.setType<CtTypedElement<*>>(it.value.type)

            if (it.value.ctExpression != null) {
                newMethod.setDefaultExpression<CtAnnotationMethod<CtMethod<Any>>>(it.value.ctExpression as CtExpression<CtMethod<Any>>?)
            }

            annotationType.addMethod<CtMethod<Any>, CtType<A>>(newMethod)
        }

        factory.Annotation().annotate(unit, annotationType.reference)

        if(targetAnnotation != null) {
            unit.removeAnnotation(targetAnnotation)
        }

    }


    public data class MethodParams(val name: String, val type: CtTypeReference<*>, val ctExpression: CtExpression<*>?)
}