package io.kartondev.strategy.flowui

import io.kartondev.strategy.MigrationStrategy
import spoon.reflect.declaration.CtType

class UiDescriptor2ViewDescriptorMigrationStrategy : MigrationStrategy {
    override fun isSupport(type: CtType<*>): Boolean {
        val isScreenType = (type.superclass != null) and
                ((type.superclass.simpleName == "StandardLookup") or
                        (type.superclass.simpleName == "StandardEditor") or
                        (type.superclass.simpleName == "StandardDetailView") or
                        (type.superclass.simpleName == "StandardScreen"))
        val annotatedByUiDescriptor = type.annotations.any { it.name.equals("UiDescriptor") }
        return isScreenType and annotatedByUiDescriptor
    }

    override fun migrate(type: CtType<*>) {
        TODO("Not yet implemented")
    }
}