package alexyzer.hextensions.fabric;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;

import static alexyzer.hextensions.util.LastMessageHandler.*;

@SuppressWarnings("UnstableApiUsage")
public class LastMessageHandlerFabric {

    public static AttachmentType<String> FABRIC_LAST_MESSAGE_ATTACHMENT;
    public static AttachmentType<String> FABRIC_MESSAGE_FILTER_ATTACHMENT;

    public static void onInitialize() {
        FABRIC_LAST_MESSAGE_ATTACHMENT = AttachmentRegistry.create(
                LAST_MESSAGE_ATTACHMENT, builder -> builder
                        .initializer(String::new)
                        .persistent(Codec.STRING)
                        .copyOnDeath()
        );

        FABRIC_MESSAGE_FILTER_ATTACHMENT = AttachmentRegistry.create(
                MESSAGE_FILTER_ATTACHMENT, builder -> builder
                        .persistent(Codec.STRING)
                        .copyOnDeath()
        );

        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(LastMessageHandlerFabric::handleMessage);

        getLastMessage = player -> player.getAttachedOrCreate(FABRIC_LAST_MESSAGE_ATTACHMENT);
        getMessageFilter = player -> player.getAttached(FABRIC_MESSAGE_FILTER_ATTACHMENT);
        setMessageFilter = (player, filter) -> player.setAttached(FABRIC_MESSAGE_FILTER_ATTACHMENT, filter);
    }

    public static boolean handleMessage(PlayerChatMessage message, ServerPlayer sender, ChatType.Bound params) {
        var messageString = message.decoratedContent().getString();

        var filter = sender.getAttached(FABRIC_MESSAGE_FILTER_ATTACHMENT);
        if (filter != null) {
            if (messageString.startsWith(filter)) {
                sender.setAttached(FABRIC_LAST_MESSAGE_ATTACHMENT, messageString.substring(filter.length()));
                return false; //Cancel event
            }
        } else {
            sender.setAttached(FABRIC_LAST_MESSAGE_ATTACHMENT, messageString);
        }

        return true;
    }
}
