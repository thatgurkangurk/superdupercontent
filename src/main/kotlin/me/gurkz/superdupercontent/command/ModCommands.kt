/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.gurkz.superdupercontent.SuperDuperContent
import me.gurkz.superdupercontent.util.Adventure
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object ModCommands {
    val superDuperContentCommand: LiteralArgumentBuilder<CommandSourceStack> = Commands.literal("super-duper-content")
        .executes { context -> run {
            val mm = MiniMessage.miniMessage()
            val parsed = mm.deserialize(
            """
                    <b>superdupercontent</b> v${SuperDuperContent.VERSION} by <color:#4fff4d><b>gurkan</b></color>

                    <u><color:#1bd96a><click:open_url:'https://modrinth.com/mod/super-duper-content'>Modrinth</click></color></u>
                    <u><click:open_url:'https://github.com/thatgurkangurk/superdupercontent'><white>GitHub</white></click></u>
                    """.trimIndent()
            )

            val audience = Adventure.audience(context.source)

            audience.sendMessage(parsed)

            return@executes 0
        } }

    fun registerCommands(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(superDuperContentCommand)
    }
}