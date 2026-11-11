package alexyzer.hextensions.util;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Misc {

    @SuppressWarnings("UnusedReturnValue") //May still be useful elsewhere, maybe
    public static boolean swapSlots(SlotAccess a, SlotAccess b) {
        var stackA = a.get();
        var stackB = b.get();
        var stackACopy = stackA.copy();
        var stackBCopy = stackB.copy();

        if (
            a.set(ItemStack.EMPTY) && a.get().isEmpty() && //No duplicates at any time
            b.set(stackACopy) && ItemStack.matches(b.get(), stackACopy) &&
            a.set(stackBCopy) && ItemStack.matches(a.get(), stackBCopy)
        ) {
            //Invalidate original stacks
            stackA.setCount(0);
            stackB.setCount(0);
            return true;
        } else {
            //Invalidate copies
            stackACopy.setCount(0);
            stackBCopy.setCount(0);
            //Rollback
            a.set(stackA);
            b.set(stackB);
            return false;
        }
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
