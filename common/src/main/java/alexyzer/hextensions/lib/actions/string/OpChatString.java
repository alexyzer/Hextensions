package alexyzer.hextensions.lib.actions.string;

import alexyzer.hextensions.util.LastMessageHandler;
import alexyzer.hextensions.Hextensions;
import alexyzer.hextensions.lib.iota.StringIota;
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.GarbageIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpChatString implements ConstMediaAction {

    public static final OpChatString INSTANCE = new OpChatString();
    public static final HexPattern PATTERN = HexPattern.fromAngleString("aade", HexDir.WEST);
    public static final ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("chat_string");

    @Override
    public int getArgc() {
        return 0;
    }

    @Override
    public @NotNull List<Iota> execute(@NotNull List<? extends Iota> list, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
        if (!(castingEnvironment.getCastingEntity() instanceof ServerPlayer player)) return List.of(new GarbageIota());
        return List.of(new StringIota(LastMessageHandler.getLastMessage.apply(player).getString()));
    }
}
