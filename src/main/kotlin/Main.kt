package io.kartondev

import com.github.javaparser.ParseResult
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.utils.SourceRoot
import java.nio.file.Path



fun main() {


    val projectRoot: Path = Path.of("")
    val roots = arrayOf(
        "",
//            "/Users/cherkasov/IdeaProjects/ats-psz/ats-psz-app/src/test/java",
    )
    for (root in roots) {
        val sourceRoot = SourceRoot(projectRoot.resolve(root))
        val parseResults: MutableList<ParseResult<CompilationUnit>> = sourceRoot.tryToParse()
        println(parseResults)
    }

}