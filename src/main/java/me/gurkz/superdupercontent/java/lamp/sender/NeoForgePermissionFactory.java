package me.gurkz.superdupercontent.java.lamp.sender;

import me.gurkz.superdupercontent.Permissions;
import me.gurkz.superdupercontent.java.lamp.actor.NeoForgeCommandActor;
import me.gurkz.superdupercontent.java.lamp.annotation.CommandPermission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.Lamp;
import revxrsal.commands.annotation.list.AnnotationList;

public enum NeoForgePermissionFactory implements revxrsal.commands.command.CommandPermission.Factory<NeoForgeCommandActor> {
    INSTANCE;

    @Override
    public @Nullable revxrsal.commands.command.CommandPermission<NeoForgeCommandActor> create(@NotNull AnnotationList annotations, @NotNull Lamp<NeoForgeCommandActor> lamp) {
        CommandPermission permissionAnn = annotations.get(CommandPermission.class);

        if (permissionAnn == null)
            return null;

        return actor -> {
            // original lamp: Permissions.check(actor.source(), permissionAnn.value(), permissionAnn.vanilla())
            return Permissions.check(actor, permissionAnn);
        };
    }
}