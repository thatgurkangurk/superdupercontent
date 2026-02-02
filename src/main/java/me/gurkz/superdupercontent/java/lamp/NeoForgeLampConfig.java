package me.gurkz.superdupercontent.java.lamp;

import me.gurkz.superdupercontent.java.lamp.actor.ActorFactory;
import me.gurkz.superdupercontent.java.lamp.actor.NeoForgeCommandActor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.Lamp;
import revxrsal.commands.LampBuilderVisitor;
import revxrsal.commands.brigadier.types.ArgumentTypes;

import java.util.function.Consumer;

import static me.gurkz.superdupercontent.java.lamp.NeoForgeVisitors.*;
import static revxrsal.commands.util.Preconditions.notNull;

/**
 * A collective immutable object that contains all Fabric-only properties and allows
 * for easy customizing and chaining using a builder
 *
 * @param <A> The actor type.
 */
public final class NeoForgeLampConfig<A extends NeoForgeCommandActor> implements LampBuilderVisitor<A> {
    private final ActorFactory<A> actorFactory;
    private final ArgumentTypes<A> argumentTypes;

    private NeoForgeLampConfig(ActorFactory<A> actorFactory, ArgumentTypes<A> argumentTypes) {
        this.actorFactory = actorFactory;
        this.argumentTypes = argumentTypes;
    }

    /**
     * Returns a new {@link Builder}.
     *
     * @param <A> The actor type
     * @return The {@link Builder}
     */
    @Contract("-> new")
    public static <A extends NeoForgeCommandActor> @NotNull Builder<A> builder() {
        return new Builder<>();
    }

    /**
     * Returns a new {@link NeoForgeLampConfig} containing the default
     * settings.
     *
     * @return The {@link Builder}
     */
    @Contract("-> new")
    public static @NotNull NeoForgeLampConfig<NeoForgeCommandActor> createDefault() {
        return new NeoForgeLampConfig<>(
                ActorFactory.defaultFactory(),
                ArgumentTypes.<NeoForgeCommandActor>builder().build()
        );
    }

    @Override public void visit(Lamp.@NotNull Builder<A> builder) {
        builder
                .accept(legacyColorCodes())
                .accept(neoForgeSenderResolver())
                .accept(neoForgeParameterTypes())
                .accept(neoForgeExceptionHandler())
                .accept(neoForgePermissions())
                .accept(registrationHooks(this));
    }

    public ActorFactory<A> actorFactory() {
        return actorFactory;
    }

    public ArgumentTypes<A> argumentTypes() {
        return argumentTypes;
    }

    /**
     * Represents a builder for {@link NeoForgeLampConfig}
     *
     * @param <A> The actor type
     */
    public static class Builder<A extends NeoForgeCommandActor> {

        private final ArgumentTypes.Builder<A> argumentTypes = ArgumentTypes.builder();
        @SuppressWarnings("unchecked")
        private ActorFactory<A> actorFactory = (ActorFactory<A>) ActorFactory.defaultFactory();

        /**
         * Sets the {@link ActorFactory}. This allows to supply custom implementations for
         * the {@link NeoForgeCommandActor} interface.
         *
         * @param actorFactory The actor factory
         * @return This builder
         * @see ActorFactory
         */
        public @NotNull Builder<A> actorFactory(@NotNull ActorFactory<A> actorFactory) {
            this.actorFactory = notNull(actorFactory, "actor factory");
            return this;
        }

        /**
         * Returns the {@link ArgumentTypes.Builder} of this builder
         *
         * @return The builder
         */
        public @NotNull ArgumentTypes.Builder<A> argumentTypes() {
            return argumentTypes;
        }

        /**
         * Applies the given {@link Consumer} on the {@link #argumentTypes()} instance.
         * This allows for easy chaining of the builder instances
         *
         * @param consumer Consumer to apply
         * @return This builder
         */
        public @NotNull Builder<A> argumentTypes(@NotNull Consumer<ArgumentTypes.Builder<A>> consumer) {
            consumer.accept(argumentTypes);
            return this;
        }

        /**
         * Returns a new {@link NeoForgeLampConfig} from this builder
         *
         * @return The newly created config
         */
        @Contract("-> new")
        public @NotNull NeoForgeLampConfig<A> build() {
            return new NeoForgeLampConfig<>(actorFactory, argumentTypes.build());
        }
    }
}