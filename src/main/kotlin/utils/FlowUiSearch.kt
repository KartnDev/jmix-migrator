package io.kartondev.utils

import spoon.reflect.declaration.CtAnnotation
import spoon.reflect.declaration.CtClass
import spoon.reflect.declaration.CtType
import spoon.reflect.factory.Factory
import spoon.support.reflect.code.CtLiteralImpl
import utils.buildPathForScreen


fun isRootRouteAnnotation(routeAnnotation: CtAnnotation<out Annotation>): Boolean {
    val parent: CtClass<*> = routeAnnotation.parent as CtClass<*>

    val pathFromRouteAnnotationExpression = routeAnnotation.values?.get("value") ?: routeAnnotation.values?.get("path")

    val path = buildPathForScreen(
        parent.simpleName,
        parent.superclass?.simpleName == "StandardEditor",
        (pathFromRouteAnnotationExpression as? CtLiteralImpl)?.value?.toString()
    )
    return path != "login" && routeAnnotation.values.containsKey("root") && (routeAnnotation.values.getValue("root") as CtLiteralImpl).value as Boolean
}

fun CtAnnotation<out Annotation>.isMainRoute(): Boolean {
    return isRootRouteAnnotation(this)
}


//@Cacheable
fun searchMainViewClass(factory: Factory): CtType<*>? {
    return factory.Class().all
        .filter { it.superclass != null &&
                (it.superclass.simpleName == "StandardLookup" ||
                        it.superclass.simpleName == "StandardEditor" ||
                        it.superclass.simpleName == "StandardDetailView" ||
                        it.superclass.simpleName == "StandardScreen" ||
                        it.superclass.simpleName == "Screen") }
        .find { ctType -> ctType.annotations.any { isRootRouteAnnotation(it) } }
}