package io.github.loshine.json.compilerplugin

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector

internal data class DebugLogger(val debug: Boolean, val messageCollector: MessageCollector) {
    fun log(message: String) {
        if (debug) {
            messageCollector.report(CompilerMessageSeverity.WARNING, message)
        }
    }
}
