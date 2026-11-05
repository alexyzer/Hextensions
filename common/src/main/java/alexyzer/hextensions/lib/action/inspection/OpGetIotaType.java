package alexyzer.hextensions.lib.action.inspection;

import alexyzer.hextensions.Hextensions;
import alexyzer.hextensions.lib.iota.StringIota;
import alexyzer.hextensions.util.Misc.UnaryOpAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class OpGetIotaType implements UnaryOpAction {

    public static final OpGetIotaType INSTANCE = new OpGetIotaType();
    public static final ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("inspection/iota_type");
    public static final HexPattern PATTERN = HexPattern.fromAngleString("wdd", HexDir.EAST);

    @Override
    public @NotNull Iota evaluate(@NotNull Iota iota, @NotNull CastingEnvironment env) {
        @SuppressWarnings("DataFlowIssue") //IotaType is expected to be registered
        var nameKey = "hexcasting.iota." + HexIotaTypes.REGISTRY.getKey(iota.getType()).toString();
        var nameString = Language.getInstance().getOrDefault(nameKey);
        return new StringIota(nameString);
    }
}
