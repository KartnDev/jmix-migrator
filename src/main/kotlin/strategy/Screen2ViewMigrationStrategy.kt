package io.kartondev.strategy

import io.kartondev.strategy.java.ClassicRoute2FlowRouteRefactor
import io.kartondev.strategy.java.JavaRefactor
import io.kartondev.strategy.java.UiController2ViewControllerRefactor
import io.kartondev.strategy.java.UiDescriptor2ViewDescriptorRefactor
import spoon.reflect.code.CtExpression
import spoon.reflect.declaration.*
import spoon.support.reflect.code.CtLiteralImpl
import utils.toRefactoredFlowUiControllerName

class Screen2ViewMigrationStrategy : MigrationStrategy {

    private val controllerRefactors: List<JavaRefactor> = listOf(
        UiController2ViewControllerRefactor(),
        UiDescriptor2ViewDescriptorRefactor(), ClassicRoute2FlowRouteRefactor()
    )

    override fun isSupport(type: CtType<*>): Boolean {
        return type.superclass != null &&
                (type.superclass.simpleName == "StandardLookup" ||
                        type.superclass.simpleName == "StandardEditor" ||
                        type.superclass.simpleName == "StandardDetailView" ||
                        type.superclass.simpleName == "StandardScreen")
    }

    override fun migrate(type: CtType<*>) {
        controllerRefactors.stream()
            .filter { it.canRefactor(type) }
            .forEach { it.makeRefactorOnTarget(type) }

    }
}

