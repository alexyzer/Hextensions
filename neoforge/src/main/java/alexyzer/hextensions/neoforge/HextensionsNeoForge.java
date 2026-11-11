package alexyzer.hextensions.neoforge;

import alexyzer.hextensions.lib.action.inspection.OpGetIotaType;
import alexyzer.hextensions.lib.action.inspection.OpGetObjectType;
import alexyzer.hextensions.lib.action.inspection.OpGetProperty;
import alexyzer.hextensions.lib.action.inspection.OpListProperties;
import alexyzer.hextensions.lib.action.manipulation.OpSwap;
import alexyzer.hextensions.lib.action.string.OpChatString;
import alexyzer.hextensions.lib.action.string.OpToNumber;
import alexyzer.hextensions.lib.action.string.OpToString;
import alexyzer.hextensions.lib.arithmetic.StringArithmetic;
import alexyzer.hextensions.lib.iota.StringIota;
import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.common.lib.HexRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

import static alexyzer.hextensions.Hextensions.MOD_ID;

@Mod(MOD_ID)
@EventBusSubscriber(modid = MOD_ID)
public final class HextensionsNeoForge {
    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(HexRegistries.IOTA_TYPE, helper ->
                helper.register(StringIota.RESOURCE_LOCATION, StringIota.TYPE));

        event.register(HexRegistries.ACTION, helper -> {
            helper.register(OpChatString.RESOURCE_LOCATION, new ActionRegistryEntry(OpChatString.PATTERN, OpChatString.INSTANCE));
            helper.register(OpToString.RESOURCE_LOCATION, new ActionRegistryEntry(OpToString.PATTERN, OpToString.INSTANCE));
            helper.register(OpToNumber.RESOURCE_LOCATION, new ActionRegistryEntry(OpToNumber.PATTERN, OpToNumber.INSTANCE));
            helper.register(OpGetObjectType.RESOURCE_LOCATION, new ActionRegistryEntry(OpGetObjectType.PATTERN, OpGetObjectType.INSTANCE));
            helper.register(OpGetIotaType.RESOURCE_LOCATION, new ActionRegistryEntry(OpGetIotaType.PATTERN, OpGetIotaType.INSTANCE));
            helper.register(OpListProperties.RESOURCE_LOCATION, new ActionRegistryEntry(OpListProperties.PATTERN, OpListProperties.INSTANCE));
            helper.register(OpGetProperty.RESOURCE_LOCATION, new ActionRegistryEntry(OpGetProperty.PATTERN, OpGetProperty.INSTANCE));
            helper.register(OpSwap.RESOURCE_LOCATION, new ActionRegistryEntry(OpSwap.PATTERN, OpSwap.INSTANCE));
        });

        event.register(HexRegistries.ARITHMETIC, helper ->
                helper.register(StringArithmetic.RESOURCE_LOCATION, StringArithmetic.INSTANCE));
    }
}
