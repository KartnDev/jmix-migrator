package io.kartondev.strategy

import spoon.reflect.declaration.CtType

interface MigrationStrategy {
    fun isSupport(type: CtType<*>): Boolean
    fun migrate(type: CtType<*>)
}