package alexyzer.hextensions.lib.action.string;

import alexyzer.hextensions.lib.iota.StringIota;
import alexyzer.hextensions.util.Misc.UnaryOpAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static alexyzer.hextensions.Hextensions.modLoc;

public class OpToNumber implements UnaryOpAction {

    public static final OpToNumber INSTANCE = new OpToNumber();
    public static final ResourceLocation RESOURCE_LOCATION = modLoc("string/tonumber");
    public static final HexPattern PATTERN = HexPattern.fromAngleString("aadqa", HexDir.WEST);

    @Override
    public @NotNull Iota evaluate(@NotNull Iota iota, @NotNull CastingEnvironment env) {
        var string = StringIota.expect(iota, 0).value;
        try {
            var number = Double.parseDouble(string);
            return new DoubleIota(number);
        } catch (NumberFormatException e) {
            return new NullIota();
        }
    }
}
