package io.github.loshine.json.compilerplugin

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

@ExperimentalCompilerApi
@AutoService(CommandLineProcessor::class) // don't forget!
class ExampleCommandLineProcessor : CommandLineProcessor {

    override val pluginId: String = "jsonPlugin"

    override val pluginOptions: Collection<CliOption> = listOf(
        CliOption(
            optionName = "enabled",
            valueDescription = "<true|false>",
            description = "whether to enable the plugin or not"
        ),
        CliOption(
            optionName = "packages",
            valueDescription = "<package.name,another.package.name,...>",
            description = "Scope of packages affected by this plugin"
        )
    )

    override fun processOption(
        option: AbstractCliOption,
        value: String,
        configuration: CompilerConfiguration
    ) = when (option.optionName) {
        "enabled" -> configuration.put(KEY_ENABLED, value.toBoolean())
        "packages" -> configuration.put(KEY_PACKAGES, value)
        else -> configuration.put(KEY_ENABLED, true)
    }
}

val KEY_ENABLED = CompilerConfigurationKey<Boolean>("whether the plugin is enabled")
val KEY_PACKAGES = CompilerConfigurationKey<String>("Scope of packages affected by this plugin")
