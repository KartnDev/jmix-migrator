package io.kartondev.strategy.java

import io.kartondev.classicASTContextGlobal
import io.kartondev.processor.annotation.AnnotationChangeParametersProcessor
import io.kartondev.processor.annotation.AnnotationChangeParametersProcessor.MethodParams
import io.kartondev.utils.isMainRoute
import io.kartondev.utils.searchMainViewClass
import spoon.reflect.declaration.CtAnnotation
import spoon.reflect.declaration.CtType
import spoon.support.reflect.code.CtLiteralImpl
import utils.buildPathForScreen

class ClassicRoute2FlowRouteRefactor : JavaRefactor {

    override fun canRefactor(type: CtType<*>): Boolean {
        return type.superclass != null &&
                (type.superclass.simpleName == "StandardLookup" ||
                        type.superclass.simpleName == "StandardEditor" ||
                        type.superclass.simpleName == "StandardDetailView" ||
                        type.superclass.simpleName == "StandardScreen" ||
                        type.superclass.simpleName == "Screen")
    }

    override fun makeRefactorOnTarget(type: CtType<*>) {
        val annotation: CtAnnotation<out Annotation>? = type.annotations.firstOrNull() { it.name.equals("Route") }
        if (annotation != null && annotation.isMainRoute()) {
            refactorMainRoute(type, annotation)
        } else {
            refactorNonMainRoute(type, annotation)
        }

    }

    private fun <T : Annotation> refactorNonMainRoute(type: CtType<*>, annotation: CtAnnotation<T>?) {
        val factory = type.factory

        val newPath = buildPathForScreenClass(type, annotation)

        val classRef = factory.Class().createReference(Class::class.java)

        AnnotationChangeParametersProcessor<T>().process(
            type,
            "com.vaadin.flow.router.Route",
            mapOf(
                "value" to MethodParams(
                    "value",
                    factory.Type().STRING,
                    factory.createCodeSnippetExpression<String>("")
                ),
                "layout" to MethodParams(
                    "layout",
                    classRef,
                    factory.createCodeSnippetExpression<Class<*>>("UI.class")
                )
            ),
            annotation,
        )
        val mainLayoutClass = classicASTContextGlobal.rootScreen

        val newAnnotation: CtAnnotation<T> =
            type.annotations.filter { it.name.equals("Route") }.get(index = 0) as CtAnnotation<T>
        newAnnotation.addValue<CtAnnotation<T>>("value", factory.Code().createLiteral(newPath))

        newAnnotation.addValue<CtAnnotation<T>>("layout", factory.Code().createClassAccess(mainLayoutClass?.reference))
    }

    private fun <T : Annotation> buildPathForScreenClass(type: CtType<*>, annotation: CtAnnotation<T>?): String {
        val isEditorScreen = type.superclass.simpleName == "StandardEditor"

        val pathFromRouteAnnotationExpression = annotation?.values?.get("value") ?: annotation?.values?.get("path")

        return buildPathForScreen(
            type.simpleName,
            isEditorScreen,
            (pathFromRouteAnnotationExpression as? CtLiteralImpl)?.value?.toString()
        )
    }

    private fun <T : Annotation> refactorMainRoute(type: CtType<*>, annotation: CtAnnotation<T>) {
        val factory = annotation.factory

        AnnotationChangeParametersProcessor<T>().process(
            type,
            "com.vaadin.flow.router.Route",
            mapOf(
                "value" to MethodParams(
                    "value",
                    factory.Type().STRING,
                    factory.createCodeSnippetExpression<String>("\"\"")
                )
            ),
            annotation
        )
        val newAnnotation: CtAnnotation<T> =
            type.annotations.filter { it.name.equals("Route") }.get(index = 0) as CtAnnotation<T>
        newAnnotation.addValue<CtAnnotation<T>>("value", factory.Code().createLiteral(""))
    }


}