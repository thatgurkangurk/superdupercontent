package me.gurkz.superdupercontent.java.lamp.annotation;

import net.minecraft.commands.SharedSuggestionProvider;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.DistributeOnMethods;
import revxrsal.commands.annotation.NotSender;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.gurkz.superdupercontent.Permissions;
import me.gurkz.superdupercontent.java.lamp.actor.NeoForgeCommandActor;

/**
 * Adds a command permission for the given command.
 */
@DistributeOnMethods
@NotSender.ImpliesNotSender
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandPermission {

    /**
     * The permission string. This is passed to {@link Permissions#check(NeoForgeCommandActor, CommandPermission)} (CommandSource, String)}
     *
     * @return The permission value
     */
    @NotNull String value();

    /**
     * The Vanilla permission value to be used as a fallback. This is passed to {@link Permissions#check(NeoForgeCommandActor, CommandPermission)}
     *
     * @return The fallback Vanilla permission value
     */
    int vanilla() default 4;

}