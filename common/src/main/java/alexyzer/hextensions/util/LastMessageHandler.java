package alexyzer.hextensions.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static alexyzer.hextensions.Hextensions.modLoc;

public class LastMessageHandler {
    public static final ResourceLocation LAST_MESSAGE_ATTACHMENT = modLoc("last_message");
    public static final ResourceLocation MESSAGE_FILTER_ATTACHMENT = modLoc("message_filter");
    public static Function<ServerPlayer, String> getLastMessage;
    public static Function<ServerPlayer, @Nullable String> getMessageFilter;
    public static BiConsumer<ServerPlayer, @Nullable String> setMessageFilter;
}
