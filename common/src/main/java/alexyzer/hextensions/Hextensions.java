package alexyzer.hextensions;

import net.minecraft.resources.ResourceLocation;

public final class Hextensions {
    public static final String MOD_ID = "hextensions";
    public static ResourceLocation modLoc(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}
