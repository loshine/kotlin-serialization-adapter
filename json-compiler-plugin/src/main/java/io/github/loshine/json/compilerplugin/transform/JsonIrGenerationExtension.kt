package io.github.loshine.json.compilerplugin.transform

import io.github.loshine.json.compilerplugin.DebugLogger
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

internal class JsonIrGenerationExtension(
    private val packages: List<String>,
    private val debugLogger: DebugLogger
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.transform(
            DataClassIrElementTransformer(pluginContext, packages, debugLogger), null
        )
    }
}
