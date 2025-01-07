package io.kartondev.processor.annotation

import io.kartondev.processor.RefactorProcessor
import spoon.reflect.code.CtExpression
import spoon.reflect.declaration.*
import spoon.support.reflect.code.CtLiteralImpl
import utils.toRefactoredFlowUiControllerName

class UiAnnotationRenameProcessor<A: Annotation> : RefactorProcessor<CtAnnotation<out A>, String> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun process(unit: CtType<*>, targetAnnotation: CtAnnotation<out A>, contextNewAnnotationName: String) {
        val factory = targetAnnotation.factory

        @Suppress("UNCHECKED_CAST")
        val annotationType: CtAnnotationType<A> = factory.Annotation().create(contextNewAnnotationName) as CtAnnotationType<A>

        val newMethod: CtAnnotationMethod<CtMethod<String>> = factory.Core().createAnnotationMethod()
        newMethod.setSimpleName<CtNamedElement>("value")
        newMethod.setType<CtTypedElement<*>>(factory.Type().STRING)
        newMethod.setDefaultExpression<CtAnnotationMethod<CtMethod<String>>>(factory.createCodeSnippetExpression("\"\""))

        annotationType.addMethod<CtMethod<String>, CtType<A>>(newMethod)

        val annotationValue: CtAnnotation<A> = factory.Annotation().annotate(unit, annotationType.reference)

        val controllerName = (targetAnnotation.getValue<CtExpression<String>>("value") as CtLiteralImpl).value
        val updatedControllerName = controllerName.toRefactoredFlowUiControllerName()

        annotationValue.addValue<CtAnnotation<A>>("value", updatedControllerName)

        unit.removeAnnotation(targetAnnotation)
    }
}