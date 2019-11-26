package com.zp4rker.fobi

import com.zp4rker.core.discord.config.JsonFile
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.AnnotatedEventManager

val config = JsonFile.loadOrDefault("config.json", true)

fun main() {
    JDABuilder(AccountType.BOT).setToken(config.getString("token")).run {
        setEventManager(AnnotatedEventManager())
        build()
    }
}