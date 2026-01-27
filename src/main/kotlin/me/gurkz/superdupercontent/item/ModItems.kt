/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.item

import com.tterrag.registrate.providers.RegistrateRecipeProvider
import me.gurkz.superdupercontent.SuperDuperContent
import me.gurkz.superdupercontent.item.custom.SilencerStickItem
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items

object ModItems {
    private val REGISTRATE = SuperDuperContent.registrate()

    @JvmStatic
    val SILENCER_STICK = REGISTRATE.item("silencer_stick", ::SilencerStickItem)
        .properties { properties -> properties.durability(32) }
        .recipe { ctx, provider ->
            run {
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                    .pattern("W  ")
                    .pattern(" A ")
                    .pattern("  A")
                    .define('A', Items.AMETHYST_SHARD)
                    .define('W', ItemTags.WOOL)
                    .unlockedBy("has_amethyst", RegistrateRecipeProvider.has(Items.AMETHYST_SHARD))
                    .unlockedBy("has_wool", RegistrateRecipeProvider.has(ItemTags.WOOL))
                    .save(provider)
            }
        }
        .model { ctx, provider -> provider.handheld(ctx) }
        .register()

    fun register() {}
}