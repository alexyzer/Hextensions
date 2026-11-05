package alexyzer.hextensions.lib.actions.inspection;

import alexyzer.hextensions.Hextensions;
import alexyzer.hextensions.util.Misc;
import alexyzer.hextensions.lib.iota.StringIota;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.api.utils.TreeList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

public class OpListProperties implements Misc.UnaryOpAction {

    public final static OpListProperties INSTANCE = new OpListProperties();
    public final static ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("inspection/list_properties");
    public final static HexPattern PATTERN = HexPattern.fromAngleString("eqqqqqa", HexDir.NORTH_EAST);

    @Override
    public @NotNull Iota evaluate(@NotNull Iota iota, @NotNull CastingEnvironment env) {
        if (!(iota instanceof Vec3Iota vecIota)) throw MishapInvalidIota.ofType(iota, 0, "vector");

        var world = env.getWorld();
        var blockPos = Misc.blockPosOf(vecIota.getVec3());
        var block = world.getBlockState(blockPos);

        var propertyList = new TreeList.TreeListBuilder<Iota>();
        for (Property<?> property : block.getProperties())
            propertyList.addOne(new StringIota(property.getName()));

        return new ListIota(propertyList.result());
    }
}
