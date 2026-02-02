package me.gurkz.superdupercontent.java.lamp.parameters;

import me.gurkz.superdupercontent.java.lamp.actor.NeoForgeCommandActor;
import me.gurkz.superdupercontent.java.lamp.exception.InvalidWorldException;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.Arrays;

/**
 * A parameter type for {@link ServerPlayer} types.
 * <p>
 * If the player inputs {@code me} or {@code self} or {@code @s}, the parser will
 * return the executing player (or give an error if the sender is not a player)
 */
public class WorldParameterType implements ParameterType<NeoForgeCommandActor, Level> {
    private static @Nullable Level getWorld(@NotNull MinecraftServer server, @NotNull String name) {
        for (ResourceKey<Level> key : server.levelKeys()) {
            if (key.location().toString().equalsIgnoreCase(name) || key.location().getPath().equalsIgnoreCase(name)) {
                return server.getLevel(key);
            }
        }
        return null;
    }

    @Override
    public Level parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<NeoForgeCommandActor> context) {
        String name = input.readString();
        if (name.equals("self") || name.equals("me") || name.equals("@s"))
            return context.actor().requirePlayer().level();
        MinecraftServer server = context.actor().source().getServer();
        Level world = getWorld(server, name);
        if (world == null)
            throw new InvalidWorldException(name);
        return world;
    }

    @Override public @NotNull SuggestionProvider<NeoForgeCommandActor> defaultSuggestions() {
        return (context) -> {
            MinecraftServer server = context.actor().source().getServer();
            return Arrays.asList(server.getPlayerList().getPlayerNamesArray());
        };
    }
}