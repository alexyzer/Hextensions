package alexyzer.hextensions.neoforge;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import static alexyzer.hextensions.util.LastMessageHandler.*;
import static alexyzer.hextensions.Hextensions.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class LastMessageHandlerNeoForge {

    public static final AttachmentType<Component> NEOFORGE_LAST_MESSAGE_ATTACHMENT = AttachmentType
            .<Component>builder(Component::empty)
            .serialize(ComponentSerialization.CODEC)
            .copyOnDeath()
            .build();

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(NeoForgeRegistries.ATTACHMENT_TYPES.key(), helper ->
                helper.register(LAST_MESSAGE_ATTACHMENT, LastMessageHandlerNeoForge.NEOFORGE_LAST_MESSAGE_ATTACHMENT));
    }

    @SubscribeEvent
    public static void onSetup(FMLCommonSetupEvent event) {
        getLastMessage = player -> player.getData(LastMessageHandlerNeoForge.NEOFORGE_LAST_MESSAGE_ATTACHMENT);
    }

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        event.getPlayer().setData(
                NEOFORGE_LAST_MESSAGE_ATTACHMENT,
                event.getMessage()
        );
    }
}
