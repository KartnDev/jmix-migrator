package io.kartondev.strategy.java

import spoon.reflect.declaration.CtType

interface JavaRefactor {
    fun canRefactor(type: CtType<*>): Boolean
    fun makeRefactorOnTarget(type: CtType<*>)
}