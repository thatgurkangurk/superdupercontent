package me.gurkz.superdupercontent.java.lamp.hooks;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import me.gurkz.superdupercontent.java.lamp.NeoForgeLampConfig;
import me.gurkz.superdupercontent.java.lamp.actor.NeoForgeCommandActor;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.Lamp;
import revxrsal.commands.brigadier.BrigadierConverter;
import revxrsal.commands.brigadier.BrigadierParser;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.hook.CancelHandle;
import revxrsal.commands.hook.CommandRegisteredHook;
import revxrsal.commands.node.ParameterNode;
import net.minecraft.commands.CommandSourceStack;

/**
 * A hook that registers Lamp commands into Fabric
 *
 * @param <A> The actor type
 */
public final class NeoForgeCommandHooks<A extends NeoForgeCommandActor> implements CommandRegisteredHook<A>, BrigadierConverter<A, CommandSourceStack> {

    private final NeoForgeLampConfig<A> config;
    private final RootCommandNode<CommandSourceStack> root = new RootCommandNode<>();
    private final BrigadierParser<CommandSourceStack, A> parser = new BrigadierParser<>(this);

    public NeoForgeCommandHooks(NeoForgeLampConfig<A> config) {
        this.config = config;
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            for (CommandNode<CommandSourceStack> child: root.getChildren()) {
                BrigadierParser.addChild(event.getDispatcher().getRoot(), child);
            }
        });
    }

    @Override
    public void onRegistered(@NotNull ExecutableCommand<A> command, @NotNull CancelHandle cancelHandle) {
        LiteralCommandNode<CommandSourceStack> node = parser.createNode(command);
        BrigadierParser.addChild(root, node);
    }

    @Override
    public @NotNull ArgumentType<?> getArgumentType(@NotNull ParameterNode<A, ?> parameter) {
        return config.argumentTypes().type(parameter);
    }

    @Override
    public @NotNull A createActor(@NotNull CommandSourceStack source, @NotNull Lamp<A> lamp) {
        return config.actorFactory().create(source, lamp);
    }
}