package alexyzer.hextensions.lib.action.inspection;

import alexyzer.hextensions.Hextensions;
import alexyzer.hextensions.lib.iota.StringIota;
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.*;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpGetProperty implements ConstMediaAction {

    public final static OpGetProperty INSTANCE = new OpGetProperty();
    public final static ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("inspection/get_property");
    public final static HexPattern PATTERN = HexPattern.fromAngleString("qeeeeed", HexDir.NORTH_EAST);

    @Override
    public int getArgc() {
        return 2;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> stack, @NotNull CastingEnvironment env) throws Mishap {
        var stackSize = stack.size();
        var target = stack.get(stackSize - 2);
        var property = StringIota.expect(stack.get(stackSize - 1), 0).value;

        if (target instanceof Vec3Iota vecIota) {
            var block = env.getWorld().getBlockState(BlockPos.containing(vecIota.getVec3()));

            for (Property<?> blockProperty : block.getProperties())
                if (blockProperty.getName().equals(property)) {
                    var value = block.getValue(blockProperty);
                    return List.of(switch (value) {
                        case Integer val -> new DoubleIota(val);
                        case Boolean val -> new BooleanIota(val);
                        case Object val -> new StringIota(val.toString());
                    });
                }

            return List.of(new NullIota());
        }

        throw MishapInvalidIota.ofType(target, 1, "vector");
    }
}
