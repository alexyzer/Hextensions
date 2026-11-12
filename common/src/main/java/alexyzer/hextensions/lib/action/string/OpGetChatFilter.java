package alexyzer.hextensions.lib.action.string;

import alexyzer.hextensions.Hextensions;
import alexyzer.hextensions.lib.iota.StringIota;
import alexyzer.hextensions.util.LastMessageHandler;
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpGetChatFilter implements ConstMediaAction {

    public static final OpGetChatFilter INSTANCE = new OpGetChatFilter();
    public static final HexPattern PATTERN = HexPattern.fromAngleString("aaedqa", HexDir.WEST);
    public static final ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("string/get_chat_filter");

    @Override
    public int getArgc() {
        return 0;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env) throws Mishap {
        if (!(env.getCastingEntity() instanceof ServerPlayer player))
            throw new MishapBadCaster();

        var filter = LastMessageHandler.getMessageFilter.apply(player);
        return List.of(
                filter != null ? new StringIota(filter) : new NullIota()
        );
    }
}
