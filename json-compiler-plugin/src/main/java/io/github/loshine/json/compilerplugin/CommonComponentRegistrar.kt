package io.github.loshine.json.compilerplugin

import com.google.auto.service.AutoService
import io.github.loshine.json.compilerplugin.transform.JsonIrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration

@ExperimentalCompilerApi
@AutoService(CompilerPluginRegistrar::class)
class CommonComponentRegistrar : CompilerPluginRegistrar() {

    override val supportsK2: Boolean
        get() = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        if (configuration[KEY_ENABLED] == false) {
            return
        }
        val packages = configuration[KEY_PACKAGES]?.split(",") ?: emptyList()

        val messageCollector =
            configuration.get(CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)
        // configuration.kotlinSourceRoots.forEach {
        //     messageCollector.report(
        //         CompilerMessageSeverity.WARNING,
        //         "*** Hello from ***" + it.path
        //     )
        // }

        val logging = true
        IrGenerationExtension.registerExtension(
            JsonIrGenerationExtension(packages, DebugLogger(logging, messageCollector))
        )
    }
}
