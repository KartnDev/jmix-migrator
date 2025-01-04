package io.kartondev.utils

import spoon.reflect.declaration.CtAnnotation
import spoon.reflect.declaration.CtType
import spoon.reflect.factory.Factory
import spoon.support.reflect.code.CtLiteralImpl


fun isRootRouteAnnotation(routeAnnotation: CtAnnotation<out Annotation>): Boolean {
    return routeAnnotation.values.containsKey("root") && (routeAnnotation.values.getValue("root") as CtLiteralImpl).value as Boolean
}

fun CtAnnotation<out Annotation>.isRootRoute(): Boolean {
    return isRootRouteAnnotation(this)
}


fun searchMainViewClass(factory: Factory): CtType<*>? {
    val find: CtType<*>? = factory.Class().all.find { ctType ->
        val routeAnnotation: List<CtAnnotation<out Annotation>> = ctType.annotations.filter { it.name == "Route" }
        return@find routeAnnotation.size == 1 &&
                routeAnnotation[0].values.containsKey("root") &&
                (routeAnnotation[0].values.getValue("root") as CtLiteralImpl).value as Boolean
    }
    return find
}