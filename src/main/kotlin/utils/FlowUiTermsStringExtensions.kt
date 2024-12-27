package utils;

public fun String.toRefactoredFlowUiControllerName(): String {
    val pattern = Regex("(?i)^(\\w+_)?(\\w+)(\\.\\w+)$")

    val result = pattern.replace(this) { matchResult ->
        val prefixAppName = matchResult.groups[1]?.value ?: ""
        var name = matchResult.groups[2]?.value ?: ""
        var postfix = matchResult.groups[3]?.value ?: ""

        name = when {
            Regex("(?i)browser").containsMatchIn(name) -> name.replace(Regex("(?i)browser"), "ListView")
            Regex("(?i)browse").containsMatchIn(name) -> name.replace(Regex("(?i)browse"), "ListView")
            Regex("(?i)editor").containsMatchIn(name) -> name.replace(Regex("(?i)editor"), "DetailView")
            Regex("(?i)edit").containsMatchIn(name) -> name.replace(Regex("(?i)edit"), "DetailView")
            else -> name
        }

        postfix = when {
            Regex("(?i)browser").containsMatchIn(postfix) -> ".list"
            Regex("(?i)browse").containsMatchIn(postfix) -> ".list"
            Regex("(?i)editor").containsMatchIn(postfix) -> ".detail"
            Regex("(?i)edit").containsMatchIn(postfix) -> ".detail"
            else -> postfix
        }

        return@replace "$prefixAppName$name$postfix"
    }

    return result
}
