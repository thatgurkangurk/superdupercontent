package me.gurkz.superdupercontent.java.lamp.actor;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.Lamp;
import revxrsal.commands.process.MessageSender;

/**
 * Represents a functional interface that allows for creating custom
 * implementations of {@link NeoForgeCommandActor} that wrap instances
 * of {@link CommandSourceStack}.
 *
 * @param <A> The actor type
 */
@FunctionalInterface
public interface ActorFactory<A extends NeoForgeCommandActor> {
    /**
     * Returns the default {@link ActorFactory} that returns a simple {@link NeoForgeCommandActor}
     * implementation
     *
     * @return The default {@link ActorFactory}.
     */
    static @NotNull ActorFactory<NeoForgeCommandActor> defaultFactory() {
        return BasicActorFactory.INSTANCE;
    }

    /**
     * Returns the default {@link ActorFactory} that returns a simple {@link NeoForgeCommandActor}
     * implementation, with custom {@link MessageSender}s for messages and errors
     *
     * @return The default {@link ActorFactory}.
     */
    static @NotNull ActorFactory<NeoForgeCommandActor> defaultFactory(
            @NotNull MessageSender<NeoForgeCommandActor, Component> messageSender,
            @NotNull MessageSender<NeoForgeCommandActor, Component> errorSender
    ) {
        return new BasicActorFactory(messageSender, errorSender);
    }

    /**
     * Creates the actor from the given {@link CommandSourceStack}
     *
     * @param sender Sender to create for
     * @param lamp   The {@link Lamp} instance
     * @return The created actor
     */
    @NotNull A create(@NotNull CommandSourceStack sender, @NotNull Lamp<A> lamp);
}