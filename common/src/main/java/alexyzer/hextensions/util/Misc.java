package alexyzer.hextensions.util;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Misc {
    public static BlockPos blockPosOf(Vec3 vec3) {
        return new BlockPos(
                (int) Math.floor(vec3.x),
                (int) Math.floor(vec3.y),
                (int) Math.floor(vec3.z)
        );
    }

    public interface UnaryOpAction extends ConstMediaAction {

        @NotNull Iota evaluate(@NotNull Iota iota, @NotNull CastingEnvironment env);

        @Override
        default int getArgc() {
            return 1;
        }

        @Override
        default @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment env) throws Mishap {
            return List.of(evaluate(list.getLast(), env));
        }
    }
}
