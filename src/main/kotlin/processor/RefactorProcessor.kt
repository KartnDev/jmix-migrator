package io.kartondev.processor

import spoon.reflect.declaration.CtType

interface RefactorProcessor<TARGET, CONTEXT> {
    fun process(unit: CtType<*>, target: TARGET, context: CONTEXT)
}