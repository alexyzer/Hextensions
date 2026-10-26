package alexyzer.hextensions.neoforge;

import alexyzer.hextensions.lib.actions.inspection.OpListProperties;
import alexyzer.hextensions.lib.actions.string.OpChatString;
import alexyzer.hextensions.lib.actions.inspection.OpIdentify;
import alexyzer.hextensions.lib.arithmetic.StringArithmetic;
import alexyzer.hextensions.lib.iota.StringIota;
import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.common.lib.HexRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

import alexyzer.hextensions.Hextensions;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import static alexyzer.hextensions.Hextensions.*;

@Mod(MOD_ID)
@EventBusSubscriber(modid = MOD_ID)
public final class HextensionsNeoForge {

    public HextensionsNeoForge() {
        // Run our common setup.
        Hextensions.init();
        NeoForgeChatHandler.init();
    }

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(HexRegistries.IOTA_TYPE, helper -> {
            helper.register(StringIota.RESOURCE_LOCATION, StringIota.TYPE);
        });
        event.register(NeoForgeRegistries.ATTACHMENT_TYPES.key(), helper -> {
            helper.register(modLoc("last_message"), NeoForgeChatHandler.LAST_MESSAGE_ATTACHMENT);
        });
        event.register(HexRegistries.ACTION, helper -> {
            helper.register(OpChatString.RESOURCE_LOCATION, new ActionRegistryEntry(OpChatString.PATTERN, OpChatString.INSTANCE));
            helper.register(OpIdentify.RESOURCE_LOCATION, new ActionRegistryEntry(OpIdentify.PATTERN, OpIdentify.INSTANCE));
            helper.register(OpListProperties.RESOURCE_LOCATION, new ActionRegistryEntry(OpListProperties.PATTERN, OpListProperties.INSTANCE));
        });
        event.register(HexRegistries.ARITHMETIC, helper -> {
            helper.register(StringArithmetic.RESOURCE_LOCATION, StringArithmetic.INSTANCE);
        });
    }
}
