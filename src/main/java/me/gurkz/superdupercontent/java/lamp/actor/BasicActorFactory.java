package me.gurkz.superdupercontent.java.lamp.actor;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.Lamp;
import revxrsal.commands.process.MessageSender;

import java.util.Objects;

/**
 * Default implementation of {@link ActorFactory}
 */
final class BasicActorFactory implements ActorFactory<NeoForgeCommandActor> {

    public static final ActorFactory<NeoForgeCommandActor> INSTANCE = new BasicActorFactory(
            NeoForgeCommandActor::sendRawMessage,
            NeoForgeCommandActor::sendRawError
    );
    private final MessageSender<NeoForgeCommandActor, Component> messageSender;
    private final MessageSender<NeoForgeCommandActor, Component> errorSender;

    /**
     *
     */
    BasicActorFactory(
            MessageSender<NeoForgeCommandActor, Component> messageSender,
            MessageSender<NeoForgeCommandActor, Component> errorSender
    ) {
        this.messageSender = messageSender;
        this.errorSender = errorSender;
    }

    @Override
    public @NotNull NeoForgeCommandActor create(@NotNull CommandSourceStack sender, @NotNull Lamp<NeoForgeCommandActor> lamp) {
        return new BasicNeoForgeActor(sender, lamp, messageSender, errorSender);
    }

    public MessageSender<NeoForgeCommandActor, Component> messageSender() {return messageSender;}

    public MessageSender<NeoForgeCommandActor, Component> errorSender() {return errorSender;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BasicActorFactory) obj;
        return Objects.equals(this.messageSender, that.messageSender) &&
                Objects.equals(this.errorSender, that.errorSender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageSender, errorSender);
    }

    @Override
    public String toString() {
        return "BasicActorFactory[" +
                "messageSender=" + messageSender + ", " +
                "errorSender=" + errorSender + ']';
    }

}