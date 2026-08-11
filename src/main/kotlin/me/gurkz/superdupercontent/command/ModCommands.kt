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
import me.gurkz.superdupercontent.PermissionNodes
import me.gurkz.superdupercontent.SuperDuperContent
import me.gurkz.superdupercontent.data.DataAttachments
import me.gurkz.superdupercontent.util.Adventure
import me.gurkz.superdupercontent.util.FireworkUtil
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.level.GameType
import net.neoforged.neoforge.server.permission.PermissionAPI

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

    val suicideDamageType = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(
        SuperDuperContent.MOD_ID, "suicide"))

    val suicideCommand: LiteralArgumentBuilder<CommandSourceStack> = Commands.literal("suicide")
        .requires { source ->
            val player = source.player ?: return@requires true

            PermissionAPI.getPermission(player, PermissionNodes.SUICIDE_COMMAND)
        }
        .executes { context -> run {
            val audience = Adventure.audience(context.source)

            val player = context.source.player

            if (player == null) {
                audience.sendFailure(SuperDuperContent.mm.deserialize("only players can run /suicide"))
                return@executes 1
            }

            if (player.gameMode.gameModeForPlayer === GameType.CREATIVE) {
                audience.sendFailure(SuperDuperContent.mm.deserialize("only players can in survival run /suicide"))
                return@executes 1
            }

            val damageSource = DamageSource(
                player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(suicideDamageType)
            )

            val pos = player.position()

            FireworkUtil.summonFirework(
                pos,
                context.source.level,
                FireworkUtil.createColour(0, 255, 0),
                FireworkUtil.createColour(255, 0, 0),
                2,
                false
            )

            player.hurt(damageSource, 20.0f)

            audience.sendSuccess(SuperDuperContent.mm.deserialize("<green>committed suicide</green>"), false)

            return@executes 0
        } }

    val lastDeathCommand: LiteralArgumentBuilder<CommandSourceStack> = Commands.literal("lastdeath")
        .requires { source ->
            val player = source.player ?: return@requires true

            PermissionAPI.getPermission(player, PermissionNodes.LAST_DEATH_COMMAND)
        }
        .executes { context -> run {
            val audience = Adventure.audience(context.source)

            val player = context.source.player

            if (player == null) {
                audience.sendFailure(SuperDuperContent.mm.deserialize("only players can run /lastdeath"))
                return@executes 1
            }

            val deathData = player.getData(DataAttachments.LAST_DEATH)

            if (deathData == null) {
                audience.sendFailure(SuperDuperContent.mm.deserialize("you don't have a death location"))
                return@executes 1
            }

            val targetLevel: ServerLevel? = player.server.getLevel(deathData.dimension)

            if (targetLevel == null) {
                audience.sendFailure(SuperDuperContent.mm.deserialize(
                    "dimension <dimension> no longer exists!",
                    Placeholder.unparsed("dimension", deathData.dimension.location().toString())
                ))
                return@executes 0
            }

            val pos = deathData.pos

            player.teleportTo(
                targetLevel,
                pos.x + 0.5,
                pos.y.toDouble(),
                pos.z + 0.5,
                deathData.yRot,
                deathData.xRot
            )

            audience.sendSuccess(SuperDuperContent.mm.deserialize("<green>woosh</green>"), false)

            return@executes 0
        } }

    fun registerCommands(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(superDuperContentCommand)
        dispatcher.register(suicideCommand)
        dispatcher.register(lastDeathCommand)
    }
}