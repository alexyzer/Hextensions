package alexyzer.hextensions.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

import static alexyzer.hextensions.Hextensions.*;

public class LastMessageHandler {
    public static final ResourceLocation LAST_MESSAGE_ATTACHMENT = modLoc("last_message");
    public static Function<ServerPlayer, Component> getLastMessage;
}
