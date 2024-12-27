package io.kartondev.strategy.flowui

import io.kartondev.strategy.MigrationStrategy
import spoon.reflect.declaration.CtType

class UiController2ViewControllerMigrationStrategy : MigrationStrategy {
    override fun isSupport(type: CtType<*>): Boolean {
        val isScreenType = (type.superclass != null) and
                ((type.superclass.simpleName == "StandardLookup") or
                        (type.superclass.simpleName == "StandardEditor") or
                        (type.superclass.simpleName == "StandardDetailView") or
                        (type.superclass.simpleName == "StandardScreen"))
        val annotatedByUiController = type.annotations.any { it.name.equals("UiController") }
        return isScreenType and annotatedByUiController
    }

    override fun migrate(type: CtType<*>) {
        val uiController = type.annotations.filter { it.name.equals("UiController") }.get(index = 0)
    }
}