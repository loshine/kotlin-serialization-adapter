package io.github.loshine.json.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

open class JsonCompilerExtension {
    var enabled: Boolean = true
    var packages: String = ""
}

class JsonCompilerGradleSubPlugin : KotlinCompilerPluginSupportPlugin {

    private var gradleExtension: JsonCompilerExtension = JsonCompilerExtension()

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        gradleExtension =
            kotlinCompilation.target.project.extensions.findByType(JsonCompilerExtension::class.java)
                ?: JsonCompilerExtension()

        return kotlinCompilation.target.project.provider {
            mutableListOf(
                SubpluginOption("enabled", gradleExtension.enabled.toString()),
                SubpluginOption("packages", gradleExtension.packages)
            )
        }
    }

    override fun apply(target: Project) {
        target.extensions.create(
            "jsonAdapter",
            JsonCompilerExtension::class.java
        )
        super.apply(target)
    }

    override fun getCompilerPluginId(): String = "jsonPlugin"

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return true
    }

    override fun getPluginArtifact() = SubpluginArtifact(
        groupId = "io.github.loshine",
        artifactId = "json-compiler-plugin",
        version = "0.0.1-SNAPSHOT" // remember to bump this version before any release!
    )
}
