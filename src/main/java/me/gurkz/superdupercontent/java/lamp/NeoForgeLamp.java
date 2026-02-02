package me.gurkz.superdupercontent.java.lamp;

import me.gurkz.superdupercontent.java.lamp.actor.NeoForgeCommandActor;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.Lamp;
import revxrsal.commands.LampBuilderVisitor;

/**
 * Includes modular building blocks for hooking into the Fabric
 * platform.
 * <p>
 * Accept individual functions using {@link Lamp.Builder#accept(LampBuilderVisitor)}
 */
public final class NeoForgeLamp {

    /**
     * Returns a {@link Lamp.Builder} that contains the default registrations
     * for the Fabric platform
     *
     * @param config The config
     * @param <A>    The actor type
     * @return A {@link Lamp.Builder}
     */
    public static <A extends NeoForgeCommandActor> Lamp.Builder<A> builder(
            @NotNull NeoForgeLampConfig<A> config
    ) {
        return Lamp.<A>builder()
                .accept(config);
    }

    /**
     * Returns a {@link Lamp.Builder} that contains the default registrations
     * for the Fabric platform
     *
     * @return A {@link Lamp.Builder}
     */
    public static Lamp.Builder<NeoForgeCommandActor> builder() {
        return builder(NeoForgeLampConfig.createDefault());
    }
}