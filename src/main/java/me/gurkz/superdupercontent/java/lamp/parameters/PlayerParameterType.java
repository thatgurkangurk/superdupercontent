package me.gurkz.superdupercontent.java.lamp.parameters;

import me.gurkz.superdupercontent.java.lamp.actor.NeoForgeCommandActor;
import me.gurkz.superdupercontent.java.lamp.exception.InvalidPlayerException;
import net.minecraft.server.level.ServerPlayer;
import revxrsal.commands.parameter.ParameterType;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.stream.MutableStringStream;

import java.util.Arrays;

/**
 * A parameter type for {@link ServerPlayer} types.
 * <p>
 * If the player inputs {@code me} or {@code self} or {@code @s}, the parser will
 * return the executing player (or give an error if the sender is not a player)
 */
public class PlayerParameterType implements ParameterType<NeoForgeCommandActor, ServerPlayer> {
    @Override
    public ServerPlayer parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<NeoForgeCommandActor> context) {
        String name = input.readString();
        if (name.equals("self") || name.equals("me") || name.equals("@s"))
            return context.actor().requirePlayer();
        ServerPlayer player = context.actor().source().getServer()
                .getPlayerList().getPlayerByName(name);
        if (player == null)
            throw new InvalidPlayerException(name);
        return player;
    }

    @Override public @NotNull SuggestionProvider<NeoForgeCommandActor> defaultSuggestions() {
        return (context) -> {
            MinecraftServer server = context.actor().source().getServer();
            return Arrays.asList(server.getPlayerList().getPlayerNamesArray());
        };
    }
}
