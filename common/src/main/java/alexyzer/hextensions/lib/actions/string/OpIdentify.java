package alexyzer.hextensions.lib.actions.string;

import alexyzer.hextensions.Hextensions;
import alexyzer.hextensions.HextensionsUtil;
import alexyzer.hextensions.lib.iota.StringIota;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidOperatorArgs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpIdentify implements HextensionsUtil.UnaryOpAction {

    public static final OpIdentify INSTANCE = new OpIdentify();
    public static final ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("identify");
    public static final HexPattern PATTERN = HexPattern.fromAngleString("eqqqqq", HexDir.NORTH_EAST);

    @Override
    public @NotNull Iota evaluate(@NotNull Iota iota, @NotNull CastingEnvironment env) {
        var world = env.getWorld();

        if (iota instanceof Vec3Iota vecIota) {
            var block = world.getBlockState(HextensionsUtil.blockPosOf(vecIota.getVec3()));
            var identifier = BuiltInRegistries.BLOCK.getKey(block.getBlock());
            return new StringIota(identifier.toString());
        }

        if (iota instanceof EntityIota entityIota) {
            var entity_type = entityIota.getEntity(world).getType();
            var identifier = BuiltInRegistries.ENTITY_TYPE.getKey(entity_type);
            return new StringIota(identifier.toString());
        }

        throw new MishapInvalidOperatorArgs(List.of(iota));
    }
}
