/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent

import com.tterrag.registrate.Registrate
import me.gurkz.superdupercontent.command.ModCommands
import me.gurkz.superdupercontent.event.SuperDuperPermissionsEvents
import me.gurkz.superdupercontent.item.ModItems
import me.gurkz.superdupercontent.util.Adventure
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.server.ServerStartedEvent
import net.neoforged.neoforge.event.server.ServerStartingEvent
import net.neoforged.neoforge.event.server.ServerStoppedEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS


@Mod(SuperDuperContent.MOD_ID)
object SuperDuperContent {
    const val MOD_ID = "superdupercontent"
    val LOGGER: Logger = LogManager.getLogger(MOD_ID)
    private val REGISTRATE by lazy { Registrate.create(MOD_ID) }
    val VERSION: String by lazy {
        ModList.get().getModContainerById(MOD_ID).map { container -> container.modInfo.version.toString() }.orElse("UNKNOWN") ?: "UNKNOWN"
    }

    @JvmStatic
    @JvmName("registrate")
    internal fun registrate() = REGISTRATE

    init {
        REGISTRATE.addRawLang("death.attack.suicide", "%1\$s committed suicide")

        ModItems.register()
        MOD_BUS.addListener(::onCommonSetup)
        NeoForge.EVENT_BUS.addListener(::onCommandRegistration)
        NeoForge.EVENT_BUS.addListener(::onServerSetup)

        NeoForge.EVENT_BUS.register(SuperDuperPermissionsEvents)

        NeoForge.EVENT_BUS.addListener { e: ServerStartingEvent -> Adventure.register(e) }
        NeoForge.EVENT_BUS.addListener { e: ServerStoppedEvent -> Adventure.deregister(e) }
    }

    fun onCommonSetup(event: FMLCommonSetupEvent) {

    }

    fun onServerSetup(event: ServerStartedEvent) {

    }

    fun onCommandRegistration(event: RegisterCommandsEvent) {
        ModCommands.registerCommands(event.dispatcher)
    }
}