package alexyzer.hextensions.fabric;

import alexyzer.hextensions.lib.action.inspection.OpGetIotaType;
import alexyzer.hextensions.lib.action.inspection.OpGetObjectType;
import alexyzer.hextensions.lib.action.inspection.OpGetProperty;
import alexyzer.hextensions.lib.action.inspection.OpListProperties;
import alexyzer.hextensions.lib.action.manipulation.OpDrop;
import alexyzer.hextensions.lib.action.manipulation.OpSwap;
import alexyzer.hextensions.lib.action.string.OpChatString;
import alexyzer.hextensions.lib.action.string.OpToNumber;
import alexyzer.hextensions.lib.action.string.OpToString;
import alexyzer.hextensions.lib.arithmetic.StringArithmetic;
import alexyzer.hextensions.lib.iota.StringIota;
import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.common.lib.HexRegistries;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

@SuppressWarnings({"unchecked", "DataFlowIssue"})
public final class HextensionsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        final Registry<IotaType<?>> IOTA_TYPE_REGISTRY = (Registry<IotaType<?>>) BuiltInRegistries.REGISTRY.get(HexRegistries.IOTA_TYPE.location());
        final Registry<ActionRegistryEntry> ACTION_REGISTRY = (Registry<ActionRegistryEntry>) BuiltInRegistries.REGISTRY.get(HexRegistries.ACTION.location());
        final Registry<Arithmetic> ARITHMETIC_REGISTRY = (Registry<Arithmetic>) BuiltInRegistries.REGISTRY.get(HexRegistries.ARITHMETIC.location());

        Registry.register(IOTA_TYPE_REGISTRY, StringIota.RESOURCE_LOCATION, StringIota.TYPE);

        Registry.register(ACTION_REGISTRY, OpChatString.RESOURCE_LOCATION, new ActionRegistryEntry(OpChatString.PATTERN, OpChatString.INSTANCE));
        Registry.register(ACTION_REGISTRY, OpToString.RESOURCE_LOCATION, new ActionRegistryEntry(OpToString.PATTERN, OpToString.INSTANCE));
        Registry.register(ACTION_REGISTRY, OpToNumber.RESOURCE_LOCATION, new ActionRegistryEntry(OpToNumber.PATTERN, OpToNumber.INSTANCE));
        Registry.register(ACTION_REGISTRY, OpGetObjectType.RESOURCE_LOCATION, new ActionRegistryEntry(OpGetObjectType.PATTERN, OpGetObjectType.INSTANCE));
        Registry.register(ACTION_REGISTRY, OpGetIotaType.RESOURCE_LOCATION, new ActionRegistryEntry(OpGetIotaType.PATTERN, OpGetIotaType.INSTANCE));
        Registry.register(ACTION_REGISTRY, OpListProperties.RESOURCE_LOCATION, new ActionRegistryEntry(OpListProperties.PATTERN, OpListProperties.INSTANCE));
        Registry.register(ACTION_REGISTRY, OpGetProperty.RESOURCE_LOCATION, new ActionRegistryEntry(OpGetProperty.PATTERN, OpGetProperty.INSTANCE));
        Registry.register(ACTION_REGISTRY, OpDrop.RESOURCE_LOCATION, new ActionRegistryEntry(OpDrop.PATTERN, OpDrop.INSTANCE));
        Registry.register(ACTION_REGISTRY, OpSwap.RESOURCE_LOCATION, new ActionRegistryEntry(OpSwap.PATTERN, OpSwap.INSTANCE));

        Registry.register(ARITHMETIC_REGISTRY, StringArithmetic.RESOURCE_LOCATION, StringArithmetic.INSTANCE);

        LastMessageHandlerFabric.onInitialize();
    }
}
