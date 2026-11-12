package alexyzer.hextensions.lib.action.string;

import alexyzer.hextensions.Hextensions;
import alexyzer.hextensions.lib.iota.StringIota;
import alexyzer.hextensions.util.LastMessageHandler;
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpSetChatFilter implements ConstMediaAction {

    public static final OpSetChatFilter INSTANCE = new OpSetChatFilter();
    public static final HexPattern PATTERN = HexPattern.fromAngleString("ddwdqa", HexDir.SOUTH_WEST);
    public static final ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("string/set_chat_filter");

    @Override
    public int getArgc() {
        return 1;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env) throws Mishap {
        if (!(env.getCastingEntity() instanceof ServerPlayer player))
            throw new MishapBadCaster();

        var filter = StringIota.expectNullable(args.getFirst(), 0);
        LastMessageHandler.setMessageFilter.accept(player, filter);

        return List.of();
    }
}
