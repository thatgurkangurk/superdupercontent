package me.gurkz.superdupercontent.java.lamp;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.Lamp;
import revxrsal.commands.LampBuilderVisitor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.CommandExceptionHandler;
import me.gurkz.superdupercontent.java.lamp.actor.NeoForgeCommandActor;
import me.gurkz.superdupercontent.java.lamp.annotation.CommandPermission;
import me.gurkz.superdupercontent.java.lamp.exception.NeoForgeExceptionHandler;
import me.gurkz.superdupercontent.java.lamp.hooks.NeoForgeCommandHooks;
import me.gurkz.superdupercontent.java.lamp.parameters.PlayerParameterType;
import me.gurkz.superdupercontent.java.lamp.parameters.WorldParameterType;
import me.gurkz.superdupercontent.java.lamp.sender.NeoForgePermissionFactory;
import me.gurkz.superdupercontent.java.lamp.sender.NeoForgeSenderResolver;

import static me.gurkz.superdupercontent.java.lamp.util.NeoForgeUtils.legacyColorize;

/**
 * Includes modular building blocks for hooking into the NeoForge
 * platform.
 * <p>
 * Accept individual functions using {@link Lamp.Builder#accept(LampBuilderVisitor)}
 */
public final class NeoForgeVisitors {

    /**
     * Makes the default format for {@link CommandActor#reply(String)} and {@link CommandActor#error(String)}
     * take the legacy ampersand ChatColor-coded format
     *
     * @param <A> The actor type
     * @return The visitor
     */
    public static <A extends NeoForgeCommandActor> @NotNull LampBuilderVisitor<A> legacyColorCodes() {
        return builder -> builder
                .defaultMessageSender((actor, message) -> actor.source().sendSystemMessage(legacyColorize(message)))
                .defaultErrorSender((actor, message) -> actor.sendRawMessage(legacyColorize("&c" + message)));
    }

    /**
     * Handles the default NeoForge exceptions
     *
     * @param <A> The actor type
     * @return The visitor
     */
    public static <A extends NeoForgeCommandActor> @NotNull LampBuilderVisitor<A> neoForgeExceptionHandler() {
        //noinspection unchecked
        return builder -> builder.exceptionHandler((CommandExceptionHandler<A>) new NeoForgeExceptionHandler());
    }

    /**
     * Resolves the sender type {@link net.minecraft.commands.CommandSourceStack} and {@link ServerPlayer}
     * for parameters that come first in the command.
     *
     * @param <A> The actor type
     * @return The visitor
     */
    public static <A extends NeoForgeCommandActor> @NotNull LampBuilderVisitor<A> neoForgeSenderResolver() {
        return builder -> builder.senderResolver(new NeoForgeSenderResolver());
    }

    /**
     * Registers the following parameter types:
     * <ul>
     *     <li>{@link ServerPlayer}</li>
     *     <li>{@link Level}</li>
     * </ul>
     *
     * @param <A> The actor type
     * @return The visitor
     */
    public static <A extends NeoForgeCommandActor> @NotNull LampBuilderVisitor<A> neoForgeParameterTypes() {
        return builder -> builder.parameterTypes()
                .addParameterTypeLast(ServerPlayer.class, new PlayerParameterType())
                .addParameterTypeLast(Level.class, new WorldParameterType());
    }

    /**
     * Adds a registration hook that injects Lamp commands into NeoForge.
     *
     * @param config The {@link NeoForgeLampConfig} instance
     * @param <A>    The actor type
     * @return The visitor
     */
    public static <A extends NeoForgeCommandActor> @NotNull LampBuilderVisitor<A> registrationHooks(
            @NotNull NeoForgeLampConfig<A> config
    ) {
        NeoForgeCommandHooks<A> hooks = new NeoForgeCommandHooks<>(config);
        return builder -> builder.hooks()
                .onCommandRegistered(hooks);
    }

    /**
     * Adds support for the {@link CommandPermission} annotation
     *
     * @param <A> The actor type
     * @return This visitor
     */
    public static <A extends NeoForgeCommandActor> @NotNull LampBuilderVisitor<A> neoForgePermissions() {
        return builder -> builder.permissionFactory(NeoForgePermissionFactory.INSTANCE);
    }
}