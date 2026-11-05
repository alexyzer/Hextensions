package alexyzer.hextensions.lib.mishap;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.GarbageIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.api.utils.TreeList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MishapStringTooBig extends Mishap {
    @Override
    public @NotNull FrozenPigment accentColor(@NotNull CastingEnvironment castingEnvironment, @NotNull Mishap.Context context) {
        //Same as MishapStackSize
        return dyeColor(DyeColor.BLACK);
    }

    @Override
    public @NotNull TreeList<Iota> execute(@NotNull CastingEnvironment env, @NotNull Mishap.Context context, @NotNull TreeList<Iota> stack) {
        return stack.appended(new GarbageIota());
    }

    @Override
    protected @Nullable Component errorMessage(@NotNull CastingEnvironment castingEnvironment, @NotNull Mishap.Context context) {
        return error("hextensions:string_too_big");
    }
}
