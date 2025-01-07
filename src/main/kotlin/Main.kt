package io.kartondev

import io.kartondev.strategy.ClassicASTContext
import io.kartondev.strategy.Screen2ViewMigrationStrategy
import io.kartondev.utils.searchMainViewClass
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.model.idea.IdeaProject
import org.gradle.tooling.model.idea.IdeaSingleEntryLibraryDependency
import spoon.Launcher
import spoon.reflect.declaration.CtType
import spoon.reflect.factory.Factory
import spoon.support.compiler.FileSystemFolder
import java.io.File

lateinit var classicASTContextGlobal: ClassicASTContext

fun main() {


    val projectDir = "/Users/cherkasov/IdeaProjects/jmix15"

    val projectLibs = getProjectLibraries(File(projectDir))

    val launcher = Launcher()
    launcher.environment.noClasspath = false

    launcher.addInputResource(FileSystemFolder(File("$projectDir/src/main/java")))

    launcher.environment.sourceClasspath = projectLibs.toTypedArray()

    launcher.buildModel()

    val factory: Factory = launcher.factory

    classicASTContextGlobal = ClassicASTContext(searchMainViewClass(factory))

    val strategies = arrayOf(Screen2ViewMigrationStrategy())

    for (ctClass: CtType<*>? in factory.Class().all) {
        for (strategy in strategies) {
            if (strategy.isSupport(ctClass!!)) {
                strategy.migrate(ctClass)
            }
        }
    }

    launcher.environment.sourceOutputDirectory = File("~/IdeaProjects/jmix15/src/main/java")
    launcher.environment.isAutoImports = true

    launcher.prettyprint()


}

fun collectDependentRepos(projectDir: File): List<String> {
    val listOfDependencies = ArrayList<String>()

    GradleConnector.newConnector()
        .forProjectDirectory(projectDir)
        .connect().use { connection ->
            val ideaProject: IdeaProject = connection.getModel(IdeaProject::class.java)

            for (module in ideaProject.modules) {

                for (dep in module.dependencies) {

                    if (dep is IdeaSingleEntryLibraryDependency) {
                        val libFile: File = dep.file
                        listOfDependencies.add(libFile.path)
                    }
                }
            }
        }
    return listOfDependencies
}


fun saveListToFile(list: List<String>, filename: String) {
    File(filename).bufferedWriter().use { writer ->
        list.forEach { line ->
            writer.write(line)
            writer.newLine()
        }
    }
}

fun readListFromFile(filename: String): List<String> {
    return File(filename).bufferedReader().readLines()
}

fun getProjectLibraries(projectPath: File): List<String> {
    val projectName = projectPath.absolutePath.split("\\").last()
    val cacheFile = "$projectName-repos.cache"

    if (File(cacheFile).exists()) {
        return readListFromFile(cacheFile)
    } else {
        val repos = collectDependentRepos(projectPath)
        saveListToFile(repos, cacheFile)
        return repos
    }
}