package me.gurkz.superdupercontent.java.lamp.actor;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import revxrsal.commands.command.CommandActor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.Lamp;
import me.gurkz.superdupercontent.java.lamp.exception.SenderNotPlayerException;
import me.gurkz.superdupercontent.java.lamp.exception.SenderNotConsoleException;
import me.gurkz.superdupercontent.java.lamp.util.NeoForgeUtils;
import revxrsal.commands.process.MessageSender;

public interface NeoForgeCommandActor extends CommandActor {
    /**
     * Returns the underlying {@link CommandSourceStack} of this actor
     *
     * @return The sender
     */
    @NotNull CommandSourceStack source();

    /**
     * Tests whether is this actor a player or not
     *
     * @return Is this a player or not
     */
    default boolean isPlayer() {
        return source().getEntity() instanceof ServerPlayer;
    }

    /**
     * Tests whether is this actor the console or not
     *
     * @return Is this the console or not
     */
    default boolean isConsole() {
        return !isPlayer();
    }

    /**
     * Returns this actor as a {@link ServerPlayer} if it is a player,
     * otherwise returns {@code null}.
     *
     * @return The sender as a player, or null.
     */
    @Nullable
    default ServerPlayer asPlayer() {
        return isPlayer() ? (ServerPlayer) source().getEntity() : null;
    }

    /**
     * Returns this actor as a {@link ServerPlayer} if it is a player,
     * otherwise throws a {@link SenderNotPlayerException}.
     *
     * @return The actor as a player
     * @throws SenderNotPlayerException if not a player
     */
    @NotNull
    default ServerPlayer requirePlayer() throws SenderNotPlayerException {
        if (!isPlayer())
            throw new SenderNotPlayerException();
        return (ServerPlayer) source().getEntity();
    }

    /**
     * Returns this actor source if it is the console, otherwise throws
     * a {@link SenderNotConsoleException}.
     *
     * @return The actor as a player
     * @throws SenderNotConsoleException if not a console
     */
    @NotNull
    default CommandSourceStack requireConsole() throws SenderNotConsoleException {
        if (!isPlayer())
            throw new SenderNotConsoleException();
        return source();
    }

    /**
     * Sends the given component to this actor.
     * <p>
     * Note that this may be delegated to an underlying {@link MessageSender},
     * as specified in an {@link ActorFactory}.
     *
     * @param message The message to send
     */
    void reply(@NotNull Component message);

    /**
     * Sends the given component to this error.
     * <p>
     * Note that this may be delegated to an underlying {@link MessageSender},
     * as specified in an {@link ActorFactory}.
     *
     * @param message The message to send
     */
    void error(@NotNull Component message);

    /**
     * Prints the given component to this actor. This function does
     * not delegate sending, but invokes {@link CommandSourceStack#sendSystemMessage(Component)}
     * directly
     *
     * @param message The message to send
     */
    default void sendRawMessage(@NotNull Component message) {
        source().sendSystemMessage(message);
    }

    /**
     * Prints the given component to this actor as an error. This function does
     * not delegate sending, but invokes {@link CommandSourceStack#sendSystemMessage(Component)}
     * directly
     *
     * @param message The message to send
     */
    default void sendRawError(@NotNull Component message) {
        source().sendSystemMessage(message.copy().withStyle(v -> v.withColor(ChatFormatting.RED)));
    }

    /**
     * Sends the given message to the actor, with legacy color-coding.
     * <p>
     * This function does
     * not delegate sending, but invokes {@link CommandSourceStack#sendSystemMessage(Component)}
     * directly
     *
     * @param message Message to send
     */
    @Override
    default void sendRawMessage(@NotNull String message) {
        source().sendSystemMessage(NeoForgeUtils.legacyColorize(message));
    }

    /**
     * Sends the given message to the actor as an error, with legacy color-coding.
     * <p>
     * This function does
     * not delegate sending, but invokes {@link CommandSourceStack#sendSystemMessage(Component)}
     * directly
     *
     * @param message Message to send
     */
    @Override
    default void sendRawError(@NotNull String message) {
        source().sendSystemMessage(NeoForgeUtils.legacyColorize("&c" + message));
    }

    /**
     * Returns the {@link Lamp} instance that constructed this actor.
     *
     * @return The {@link Lamp} instance
     */
    @Override Lamp<NeoForgeCommandActor> lamp();

    /**
     * Returns the name of this actor. Varies depending on the
     * platform.
     *
     * @return The actor name
     */
    @Override @NotNull
    default String name() {
        return isConsole() ? "Console" : requirePlayer().getName().getString();
    }
}
