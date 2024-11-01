package io.kartondev.strategy

import spoon.reflect.code.CtExpression
import spoon.reflect.declaration.*
import spoon.support.reflect.code.CtLiteralImpl

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
        val factory = annotation.factory

        @Suppress("UNCHECKED_CAST")
        val annotationType: CtAnnotationType<A> = factory.Annotation().create(newAnnotationName) as CtAnnotationType<A>

        val newMethod: CtAnnotationMethod<CtMethod<String>> = factory.Core().createAnnotationMethod()
        newMethod.setSimpleName<CtNamedElement>("value")
        newMethod.setType<CtTypedElement<*>>(factory.Type().STRING)
        newMethod.setDefaultExpression<CtAnnotationMethod<CtMethod<String>>>(factory.createCodeSnippetExpression("\"\""))

        annotationType.addMethod<CtMethod<String>, CtType<A>>(newMethod)

        val annotationValue: CtAnnotation<A> = factory.Annotation().annotate(type, annotationType.reference)

        val controllerName = (annotation.getValue<CtExpression<String>>("value") as CtLiteralImpl).value
        val updatedControllerName = processUiTerms2FlowuiRename(controllerName)

        annotationValue.addValue<CtAnnotation<A>>("value", updatedControllerName)

        type.removeAnnotation(annotation)
    }
    private fun processUiTerms2FlowuiRename(input: String): String {
        val pattern = Regex("(?i)^(\\w+_)?(\\w+)(\\.\\w+)$")

        val result = pattern.replace(input) { matchResult ->
            val appname = matchResult.groups[1]?.value ?: ""
            var name = matchResult.groups[2]?.value ?: ""
            var postfix = matchResult.groups[3]?.value ?: ""

            name = when {
                Regex("(?i)browser").containsMatchIn(name) -> name.replace(Regex("(?i)browser"), "ListView")
                Regex("(?i)browse").containsMatchIn(name) -> name.replace(Regex("(?i)browse"), "ListView")
                Regex("(?i)editor").containsMatchIn(name) -> name.replace(Regex("(?i)editor"), "DetailView")
                Regex("(?i)edit").containsMatchIn(name) -> name.replace(Regex("(?i)edit"), "DetailView")
                else -> name
            }

            postfix = when {
                Regex("(?i)browser").containsMatchIn(postfix) -> ".list"
                Regex("(?i)browse").containsMatchIn(postfix) -> ".list"
                Regex("(?i)editor").containsMatchIn(postfix) -> ".detail"
                Regex("(?i)edit").containsMatchIn(postfix) -> ".detail"
                else -> postfix
            }

            "$appname$name$postfix"
        }

        return result
    }

}