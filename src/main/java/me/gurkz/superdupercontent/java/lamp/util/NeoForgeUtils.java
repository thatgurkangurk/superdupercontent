package me.gurkz.superdupercontent.java.lamp.util;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import static revxrsal.commands.util.Preconditions.cannotInstantiate;

public final class NeoForgeUtils {
    private NeoForgeUtils() {
        cannotInstantiate(NeoForgeUtils.class);
    }

    /**
     * Returns a {@link Component} that colorizes the given text using
     * the ampersand for color coding.
     *
     * @param text The text to colorize
     * @return The component
     */
    public static @NotNull Component legacyColorize(@NotNull String text) {
        return Component.literal(text.replace('&', '§'));
    }
}
