package alexyzer.hextensions.neoforge;

import alexyzer.hextensions.lib.iota.StringIota;
import at.petrak.hexcasting.common.lib.HexRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

import alexyzer.hextensions.Hextensions;
import net.neoforged.neoforge.registries.RegisterEvent;

import static alexyzer.hextensions.Hextensions.MOD_ID;
import static alexyzer.hextensions.Hextensions.modLoc;

@Mod(MOD_ID)
@EventBusSubscriber(modid = MOD_ID)
public final class HextensionsNeoForge {

    public HextensionsNeoForge() {
        // Run our common setup.
        Hextensions.init();
    }

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(HexRegistries.IOTA_TYPE, helper -> {
            helper.register(modLoc("string"), StringIota.TYPE);
        });
    }
}
