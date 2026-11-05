package alexyzer.hextensions.lib.action.string;

import alexyzer.hextensions.lib.iota.StringIota;
import alexyzer.hextensions.util.Misc.UnaryOpAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static alexyzer.hextensions.Hextensions.modLoc;

public class OpToString implements UnaryOpAction {

    public static final OpToString INSTANCE = new OpToString();
    public static final ResourceLocation RESOURCE_LOCATION = modLoc("string/tostring");
    public static final HexPattern PATTERN = HexPattern.fromAngleString("aad", HexDir.WEST);

    @Override
    public @NotNull Iota evaluate(@NotNull Iota iota, @NotNull CastingEnvironment env) {
        return new StringIota(getString(iota, env));
    }

    private static @NotNull String getString(@NotNull Iota iota, @NotNull CastingEnvironment env) {
        return switch (iota) {
            case EntityIota entityIota -> getRawName(iota, entityIota);
            case ListIota listIota -> processList(env, listIota);
            case StringIota stringIota -> stringIota.value;
            default -> iota.display().getString();
        };
    }

    public static @NotNull String getRawName(@NotNull Iota iota, EntityIota entityIota) {
        var entityName = entityIota.getRawName();
        return entityName != null ?
                entityName :
                iota.display().getString(); /*An unknown entity*/
    }

    public static @NotNull String processList(@NotNull CastingEnvironment env, ListIota listIota) {
        var list = listIota.getList();

        var stringBuilder = new StringBuilder();
        stringBuilder.append('[');

        var listIterator = list.iterator();
        if (listIterator.hasNext()) {
            var /*first*/ previous = listIterator.next();
            stringBuilder.append(getString(previous, env));

            while (listIterator.hasNext()) {
                var current = listIterator.next();

                if (previous.getType().usesListCommas() || current.getType().usesListCommas())
                    stringBuilder.append(", ");
                stringBuilder.append(getString(current, env));

                previous = current;
            }
        }

        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
