package alexyzer.hextensions;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

public final class Hextensions {
    public static final String MOD_ID = "hextensions";


    public static ResourceLocation modLoc(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static Function<ServerPlayer, Component> getLastMessage;

    public static void init() {
        // Write common init code here.
    }
}
