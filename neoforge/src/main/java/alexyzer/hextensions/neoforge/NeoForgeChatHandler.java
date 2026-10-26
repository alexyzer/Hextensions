package alexyzer.hextensions.neoforge;

import alexyzer.hextensions.Hextensions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.ServerChatEvent;

import static alexyzer.hextensions.Hextensions.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class NeoForgeChatHandler {

    public static final AttachmentType<Component> LAST_MESSAGE_ATTACHMENT =
            AttachmentType.<Component>builder(
                    () -> Component.literal(""))
                    .serialize(ComponentSerialization.CODEC)
                    .copyOnDeath()
                    .build();

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        event.getPlayer().setData(
                LAST_MESSAGE_ATTACHMENT,
                event.getMessage()
        );
    }

    public static void init() {
        Hextensions.getLastMessage = player -> player
                .getData(NeoForgeChatHandler.LAST_MESSAGE_ATTACHMENT);
    }
}
