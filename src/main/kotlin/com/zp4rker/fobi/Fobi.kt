package com.zp4rker.fobi

import com.zp4rker.core.discord.command.CommandHandler
import com.zp4rker.core.discord.config.JsonFile
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.AnnotatedEventManager

val config = JsonFile.loadOrDefault("config.json", true)
val VERSION = "v${object {}.javaClass.`package`.implementationVersion}"

fun main() {
    val handler = CommandHandler(config.getString("prefix")).apply {
        registerCommands(com.zp4rker.fobi.commands.dev.Update)
    }

    with(JDABuilder(AccountType.BOT)) {
        setToken(config.getString("token"))
        setEventManager(AnnotatedEventManager())
        addEventListeners(handler)
        setActivity(Activity.listening("dev instructions"))
        setStatus(OnlineStatus.DO_NOT_DISTURB)
    }.build()
}