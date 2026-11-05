package alexyzer.hextensions.lib.actions.inspection;

import alexyzer.hextensions.Hextensions;
import alexyzer.hextensions.util.Misc;
import alexyzer.hextensions.lib.iota.StringIota;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidOperatorArgs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpGetObjectType implements Misc.UnaryOpAction {

    public static final OpGetObjectType INSTANCE = new OpGetObjectType();
    public static final ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("object_type");
    public static final HexPattern PATTERN = HexPattern.fromAngleString("waa", HexDir.EAST);

    @Override
    public @NotNull Iota evaluate(@NotNull Iota iota, @NotNull CastingEnvironment env) {
        var world = env.getWorld();

        var nameKey = switch (iota) {
            case Vec3Iota vecIota -> {
                var blockPos = Misc.blockPosOf(vecIota.getVec3());
                env.assertPosInRange(blockPos);
                var block = world.getBlockState(blockPos);
                yield BuiltInRegistries.BLOCK.getKey(block.getBlock()).toLanguageKey("block");
            }
            case EntityIota entityIota -> {
                //Entity is not required to be in reach
                var entity_type = entityIota.getEntity(world).getType();
                yield BuiltInRegistries.ENTITY_TYPE.getKey(entity_type).toLanguageKey("entity");
            }
            default -> throw new MishapInvalidOperatorArgs(List.of(iota));
        };

        var nameString = Language.getInstance().getOrDefault(nameKey);
        return new StringIota(nameString);
    }
}
