package alexyzer.hextensions.fabric;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;

import static alexyzer.hextensions.util.LastMessageHandler.*;

@SuppressWarnings("UnstableApiUsage")
public class LastMessageHandlerFabric {

    public static AttachmentType<Component> FABRIC_LAST_MESSAGE_ATTACHMENT;

    public static void onInitialize() {
        FABRIC_LAST_MESSAGE_ATTACHMENT = AttachmentRegistry.create(
                LAST_MESSAGE_ATTACHMENT, builder -> builder
                        .initializer(Component::empty)
                        .persistent(ComponentSerialization.CODEC)
                        .copyOnDeath()
        );

        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(LastMessageHandlerFabric::handleMessage);

        getLastMessage = player -> player.getAttachedOrCreate(FABRIC_LAST_MESSAGE_ATTACHMENT);
    }

    public static boolean handleMessage(PlayerChatMessage message, ServerPlayer sender, ChatType.Bound params) {
        sender.setAttached(FABRIC_LAST_MESSAGE_ATTACHMENT, message.decoratedContent());
        return true;
    }
}
