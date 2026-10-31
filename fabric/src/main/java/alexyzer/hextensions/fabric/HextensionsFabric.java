package alexyzer.hextensions.fabric;

import alexyzer.hextensions.lib.actions.inspection.OpGetProperty;
import alexyzer.hextensions.lib.actions.inspection.OpIdentify;
import alexyzer.hextensions.lib.actions.inspection.OpListProperties;
import alexyzer.hextensions.lib.actions.string.OpChatString;
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
        Registry.register(ACTION_REGISTRY, OpIdentify.RESOURCE_LOCATION, new ActionRegistryEntry(OpIdentify.PATTERN, OpIdentify.INSTANCE));
        Registry.register(ACTION_REGISTRY, OpListProperties.RESOURCE_LOCATION, new ActionRegistryEntry(OpListProperties.PATTERN, OpListProperties.INSTANCE));
        Registry.register(ACTION_REGISTRY, OpGetProperty.RESOURCE_LOCATION, new ActionRegistryEntry(OpGetProperty.PATTERN, OpGetProperty.INSTANCE));

        Registry.register(ARITHMETIC_REGISTRY, StringArithmetic.RESOURCE_LOCATION, StringArithmetic.INSTANCE);

        LastMessageHandlerFabric.onInitialize();
    }
}
