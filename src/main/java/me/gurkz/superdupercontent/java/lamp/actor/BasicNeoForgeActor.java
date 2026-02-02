package me.gurkz.superdupercontent.java.lamp.actor;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.Lamp;
import revxrsal.commands.process.MessageSender;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

final class BasicNeoForgeActor implements NeoForgeCommandActor {
    private static final UUID CONSOLE_UUID = new UUID(0, 0);
    private final CommandSourceStack sender;
    private final Lamp<NeoForgeCommandActor> lamp;
    private final MessageSender<NeoForgeCommandActor, Component> messageSender;
    private final MessageSender<NeoForgeCommandActor, Component> errorSender;

    BasicNeoForgeActor(
            CommandSourceStack sender,
            Lamp<NeoForgeCommandActor> lamp,
            MessageSender<NeoForgeCommandActor, Component> messageSender,
            MessageSender<NeoForgeCommandActor, Component> errorSender
    ) {
        this.sender = sender;
        this.lamp = lamp;
        this.messageSender = messageSender;
        this.errorSender = errorSender;
    }

    @Override public @NotNull CommandSourceStack source() {
        return sender;
    }

    @Override public void reply(@NotNull Component message) {
        messageSender.send(this, message);
    }

    @Override public void error(@NotNull Component message) {
        errorSender.send(this, message);
    }

    @Override public @NotNull UUID uniqueId() {
        if (sender.getEntity() != null)
            return sender.getEntity().getUUID();
        else if (isConsole())
            return CONSOLE_UUID;
        else
            return UUID.nameUUIDFromBytes(name().getBytes(StandardCharsets.UTF_8));
    }

    @Override public Lamp<NeoForgeCommandActor> lamp() {
        return lamp;
    }

    public CommandSourceStack sender() {return sender;}

    public MessageSender<NeoForgeCommandActor, Component> messageSender() {return messageSender;}

    public MessageSender<NeoForgeCommandActor, Component> errorSender() {return errorSender;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BasicNeoForgeActor) obj;
        return Objects.equals(this.sender, that.sender) &&
                Objects.equals(this.lamp, that.lamp) &&
                Objects.equals(this.messageSender, that.messageSender) &&
                Objects.equals(this.errorSender, that.errorSender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, lamp, messageSender, errorSender);
    }

    @Override
    public String toString() {
        return "BasicFabricActor[" +
                "sender=" + sender + ", " +
                "lamp=" + lamp + ", " +
                "messageSender=" + messageSender + ", " +
                "errorSender=" + errorSender + ']';
    }

}