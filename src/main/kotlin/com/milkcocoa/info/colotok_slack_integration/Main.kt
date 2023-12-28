package com.milkcocoa.info.colotok_slack_integration

import com.milkcocoa.info.colotok.core.formatter.builtin.text.DetailTextFormatter
import com.milkcocoa.info.colotok.core.formatter.builtin.text.SimpleTextFormatter
import com.milkcocoa.info.colotok.core.logger.LogLevel
import com.milkcocoa.info.colotok.core.logger.LoggerFactory
import com.milkcocoa.info.colotok.core.provider.builtin.ConsoleProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.Security.addProvider

class Main {

}

fun main(){
    val logger = LoggerFactory()
        .addProvider(ConsoleProvider{
            formatter = SimpleTextFormatter
            logLevel = LogLevel.DEBUG
        })
        .addProvider(SlackProvider{
            webhook_url = "<your slack webhook url>"
            formatter = DetailTextFormatter
            logLevel = LogLevel.WARN
        })
        .getLogger()

    logger.trace("TRACE LEVEL LOG")
    logger.debug("DEBUG LEVEL LOG")
    logger.info("INFO LEVEL LOG")
    logger.warn("WARN LEVEL LOG")
    logger.error("ERROR LEVEL LOG")
    Thread.sleep(5_000)
}