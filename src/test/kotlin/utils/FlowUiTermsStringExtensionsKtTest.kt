package utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class FlowUiTermsStringExtensionsKtTest {

    @Test
    fun toRefactoredFlowUiControllerName_SimpleControllerName() {
        val controllerNameClassic = "User.browse"

        val controllerNameFlowUi = controllerNameClassic.toRefactoredFlowUiControllerName()

        assertEquals("User.list", controllerNameFlowUi)
    }

    @Test
    fun toRefactoredFlowUiControllerName_AdvancedControllerName() {
        val controllerNameClassic = "UserBrowser.browse"

        val controllerNameFlowUi = controllerNameClassic.toRefactoredFlowUiControllerName()

        assertEquals("UserListView.list", controllerNameFlowUi)
    }

    @Test
    fun toRefactoredFlowUiControllerName_SimpleControllerName_Reversed() {
        val controllerNameClassic = "User.browser"

        val controllerNameFlowUi = controllerNameClassic.toRefactoredFlowUiControllerName()

        assertEquals("User.list", controllerNameFlowUi)
    }

    @Test
    fun toRefactoredFlowUiControllerName_AdvancedControllerName_Reversed() {
        val controllerNameClassic = "UserBrowse.browser"

        val controllerNameFlowUi = controllerNameClassic.toRefactoredFlowUiControllerName()

        assertEquals("UserListView.list", controllerNameFlowUi)
    }

    @Test
    fun toRefactoredFlowUiControllerName_RandomName() {
        val controllerNameClassic = "aws_CategoryBrowser.browse"

        val controllerNameFlowUi = controllerNameClassic.toRefactoredFlowUiControllerName()

        assertEquals("aws_CategoryListView.list", controllerNameFlowUi)
    }
}