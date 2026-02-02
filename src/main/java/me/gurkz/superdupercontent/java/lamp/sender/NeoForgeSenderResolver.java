package me.gurkz.superdupercontent.java.lamp.sender;

import me.gurkz.superdupercontent.java.lamp.actor.NeoForgeCommandActor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandParameter;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.process.SenderResolver;

public final class NeoForgeSenderResolver implements SenderResolver<NeoForgeCommandActor> {

    @Override public boolean isSenderType(@NotNull CommandParameter parameter) {
        return CommandSourceStack.class.isAssignableFrom(parameter.type());
    }

    public @NotNull Object getSender(@NotNull Class<?> customSenderType, @NotNull NeoForgeCommandActor actor, @NotNull ExecutableCommand<NeoForgeCommandActor> command) {
        if (ServerPlayer.class.isAssignableFrom(customSenderType))
            return actor.requirePlayer();
        return actor.source();
    }
}