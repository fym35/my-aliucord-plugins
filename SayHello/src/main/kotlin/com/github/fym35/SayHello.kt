package com.github.fym35

import android.content.Context
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI
import com.aliucord.entities.Plugin
import com.discord.api.commands.ApplicationCommandType

@AliucordPlugin(requiresRestart = false)
class SayHello : Plugin() {
    override fun start(context: Context) {

        commands.registerCommand("hellowitharguments", "Say hello to someone", listOf(
            Utils.createCommandOption(ApplicationCommandType.STRING, "name", "Person to say hello to"),
            Utils.createCommandOption(ApplicationCommandType.USER, "user", "User to say hello to")
        )) { ctx ->
            if (ctx.containsArg("user")) {
                val user = ctx.getRequiredUser("user")
                CommandsAPI.CommandResult("Hello ${user.username}!")
            } else {
                val name = ctx.getStringOrDefault("name", "World")
                CommandsAPI.CommandResult("Hello $name!")
            }
        }
    }

    override fun stop(context: Context) {
        commands.unregisterAll()
    }
}
