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

/**
 * Builds a URL path for the given screen based on its class name and screen type.
 * If [pathForScreen] is present (e.g., from @Route), only the first segment before a slash is taken.
 *
 * @param screenClassName The simple name of the screen class (e.g., "UserBrowse", "UserEdit").
 * @param isEditorScreen Indicates whether the screen is an editor (e.g., "StandardEditor").
 * @param pathForScreen The optional predefined path. If provided, the part before the first slash
 *                      has priority over the class name.
 *
 * @return A path in the format "domain" for non-editor screens or "domain/:id" for editor screens.
 *         Example outputs: "user", "user/:id".
 *
 * Examples:
 *  - If pathForScreen = "users/edit", we take "users".
 *  - If pathForScreen = "users", we take "users".
 *  - If no pathForScreen is given, and screenClassName = "UserBrowse", we derive "user".
 *  - If isEditorScreen = true, we append "/:id".
 */
fun buildPathForScreen(
    screenClassName: String,
    isEditorScreen: Boolean,
    pathForScreen: String?
): String {
    // Extract the domain from the route (if present), ignoring extra segments.
    val domainNameFromPath = pathForScreen
        ?.removeSurrounding("\"")   // remove quotes if any
        ?.trim()
        ?.substringBefore("/")      // ignore anything after the first slash

    // Derive the domain from the screen class name if no (or empty) path is given
    val rawDomain = if (!domainNameFromPath.isNullOrBlank()) {
        domainNameFromPath
    } else {
        screenClassName
            .removeSuffix("Browse")
            .removeSuffix("Browser")
            .removeSuffix("Edit")
            .removeSuffix("Editor")
    }

    // Lowercase the first character for consistency
    val domain = rawDomain.replaceFirstChar { it.lowercaseChar() }

    // Append "/:id" if it's an editor screen
    return if (isEditorScreen) "$domain/:id" else domain
}


