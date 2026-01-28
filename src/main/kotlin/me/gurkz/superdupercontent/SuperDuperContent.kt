/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent

import com.tterrag.registrate.Registrate
import me.gurkz.superdupercontent.item.ModItems
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(SuperDuperContent.MOD_ID)
object SuperDuperContent {
    const val MOD_ID = "superdupercontent"
    val LOGGER: Logger = LogManager.getLogger(MOD_ID)
    private val REGISTRATE by lazy { Registrate.create(MOD_ID) }

    @JvmStatic
    @JvmName("registrate")
    internal fun registrate() = REGISTRATE

    init {
        ModItems.register()
        MOD_BUS.addListener(::onCommonSetup)
    }

    fun onCommonSetup(event: FMLCommonSetupEvent) {
        LOGGER.info("hello from superdupercontent")
    }
}