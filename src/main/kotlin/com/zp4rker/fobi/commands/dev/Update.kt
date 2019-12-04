package com.zp4rker.fobi.commands.dev

import com.zp4rker.core.discord.command.Command
import com.zp4rker.fobi.VERSION
import com.zp4rker.fobi.config
import com.zp4rker.fobi.updater.checkUpdates
import com.zp4rker.fobi.updater.download
import com.zp4rker.fobi.updater.restart
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

object Update : Command(aliases = arrayOf("update"), description = "Updates & restarts the bot", usage = "update", autoDelete = true, hidden = true) {

    override fun handle(message: Message, channel: TextChannel, guild: Guild, args: List<String>) {
        // make sure admin
        if (message.author.id != config.getString("admin")) return

        // check for updates
        if (checkUpdates()) {
            // base embed
            val embed = EmbedBuilder().setTitle("Updating")

            // start message
            val msg = channel.sendMessage(embed.setDescription("Downloading latest version...").build()).complete()

            // start download
            download()

            // update message
            msg.editMessage(embed.setDescription("Successfully downloaded! Rebooting...").build()).complete()

            // restart
            restart()
        } else {
            // already up-to-date
            with(EmbedBuilder()) {
                setTitle("No updates found")
                setDescription("$VERSION is already the latest version!")
                build()
            }.also { channel.sendMessage(it).queue() }
        }
    }
}