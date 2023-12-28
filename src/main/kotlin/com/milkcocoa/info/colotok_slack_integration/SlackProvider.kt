package com.milkcocoa.info.colotok_slack_integration

import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.kittinunf.fuel.coroutines.awaitUnit
import com.github.kittinunf.fuel.httpPost
import com.milkcocoa.info.colotok.core.formatter.builtin.text.DetailTextFormatter
import com.milkcocoa.info.colotok.core.formatter.details.Formatter
import com.milkcocoa.info.colotok.core.formatter.details.LogStructure
import com.milkcocoa.info.colotok.core.logger.LogLevel
import com.milkcocoa.info.colotok.core.provider.details.Provider
import com.milkcocoa.info.colotok.core.provider.details.ProviderConfig
import com.milkcocoa.info.colotok.core.provider.rotation.Rotation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import java.net.HttpURLConnection
import java.net.URLConnection

class SlackProvider(config: SlackProviderConfig): Provider {

    constructor(config: SlackProviderConfig.() -> Unit): this(SlackProviderConfig().apply(config))

    class SlackProviderConfig() : ProviderConfig {
        var webhook_url: String = ""

        override var logLevel: LogLevel = LogLevel.DEBUG

        override var formatter: Formatter = DetailTextFormatter

        /**
         * no effect
         */
        override var colorize: Boolean = false
    }

    private val webhookUrl = config.webhook_url
    private val logLevel = config.logLevel
    private val formatter = config.formatter

    override fun write(name: String, msg: String, level: LogLevel) {
        if(level.isEnabledFor(logLevel).not()){
            return
        }
        kotlin.runCatching {
            webhookUrl.httpPost()
                .appendHeader("Content-Type" to "application/json")
                .body("""
            {"text": "${formatter.format(msg, level)}"}
        """.trimIndent())
                .response { _, _, _ -> }
        }.getOrElse { println(it) }
    }

    override fun <T : LogStructure> write(name: String, msg: T, serializer: KSerializer<T>, level: LogLevel) {
        if(level.isEnabledFor(logLevel).not()){
            return
        }
        kotlin.runCatching {
            webhookUrl.httpPost()
                .appendHeader("Content-Type" to "application/json")
                .body("""
            {"text": "${formatter.format(msg, serializer, level)}"}
        """.trimIndent())
                .response { _, _, _ -> }
        }.getOrElse { println(it) }
    }
}