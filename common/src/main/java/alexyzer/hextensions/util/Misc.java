package alexyzer.hextensions.util;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Misc {

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
