/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.item;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import me.gurkz.superdupercontent.SuperDuperContent;
import me.gurkz.superdupercontent.item.custom.SilencerStickItem;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

public class ModItems {
    private static final Registrate REGISTRATE = SuperDuperContent.registrate();

    public static final ItemEntry<SilencerStickItem> SILENCER_STICK = REGISTRATE
            .item("silencer_stick", SilencerStickItem::new)
            .properties((properties -> properties.durability(32)))
            .recipe((ctx, provider) -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                        .pattern("A  ")
                        .pattern(" A ")
                        .pattern("  A")
                        .define('A', Items.AMETHYST_SHARD)
                        .unlockedBy("has_amethyst", RegistrateRecipeProvider.has(Items.AMETHYST_SHARD)).save(provider);
            })
            .model((ctx, provider) -> provider.handheld(ctx))
            .register();

    public static void register() {}
}
