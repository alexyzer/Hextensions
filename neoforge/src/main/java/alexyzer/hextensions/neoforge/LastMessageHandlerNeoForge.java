package alexyzer.hextensions.neoforge;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import static alexyzer.hextensions.Hextensions.MOD_ID;
import static alexyzer.hextensions.util.LastMessageHandler.*;

@EventBusSubscriber(modid = MOD_ID)
public class LastMessageHandlerNeoForge {

    public static final AttachmentType<String> NEOFORGE_LAST_MESSAGE_ATTACHMENT = AttachmentType
            .builder(() -> "")
            .serialize(Codec.STRING)
            .copyOnDeath()
            .build();

    public static final AttachmentType<String> NEOFORGE_MESSAGE_FILTER_ATTACHMENT = AttachmentType
            .builder(() -> "")
            .serialize(Codec.STRING)
            .copyOnDeath()
            .build();

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(NeoForgeRegistries.ATTACHMENT_TYPES.key(), helper -> {
            helper.register(LAST_MESSAGE_ATTACHMENT, LastMessageHandlerNeoForge.NEOFORGE_LAST_MESSAGE_ATTACHMENT);
            helper.register(MESSAGE_FILTER_ATTACHMENT, LastMessageHandlerNeoForge.NEOFORGE_MESSAGE_FILTER_ATTACHMENT);
        });
    }

    @SubscribeEvent
    public static void onSetup(FMLCommonSetupEvent event) {
        getLastMessage = player -> player.getData(LastMessageHandlerNeoForge.NEOFORGE_LAST_MESSAGE_ATTACHMENT);
        getMessageFilter = player -> player.getExistingData(LastMessageHandlerNeoForge.NEOFORGE_MESSAGE_FILTER_ATTACHMENT).orElse(null);
        setMessageFilter = (player, filter) -> {
            if (filter != null) player.setData(LastMessageHandlerNeoForge.NEOFORGE_MESSAGE_FILTER_ATTACHMENT, filter);
            else player.removeData(NEOFORGE_MESSAGE_FILTER_ATTACHMENT);
        };
    }

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        var player = event.getPlayer();
        var messageString = event.getRawText();

        var filter = player.getExistingData(NEOFORGE_MESSAGE_FILTER_ATTACHMENT).orElse(null);
        if (filter != null) {
            if (messageString.startsWith(filter)) {
                player.setData(NEOFORGE_LAST_MESSAGE_ATTACHMENT, messageString.substring(filter.length()));
                event.setCanceled(true);
            }
        } else {
            player.setData(NEOFORGE_LAST_MESSAGE_ATTACHMENT, messageString);
        }
    }
}
