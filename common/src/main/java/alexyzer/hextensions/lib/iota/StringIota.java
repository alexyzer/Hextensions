package alexyzer.hextensions.lib.iota;

import alexyzer.hextensions.Hextensions;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringIota extends Iota {

    public final static int MAX_SIZE = 1000;
    public final static ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("string");

    public @NotNull String value;

    public StringIota(@NotNull String value) {
        super(() -> TYPE);
        this.value = value;
    }

    @Override
    public boolean isTruthy(@Nullable CastingEnvironment castingEnvironment) {
        return !value.isEmpty();
    }

    @Override
    protected boolean toleratesOther(Iota iota) {
        if (!(iota instanceof StringIota otherString)) return false;
        return otherString.value.equals(this.value);
    }

    @Override
    public Component display() {
        return Component.literal(String.format("\"%s\"", value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static final IotaType<StringIota> TYPE = new IotaType<>() {

        public static final MapCodec<StringIota> CODEC = Codec
                .sizeLimitedString(MAX_SIZE)
                .xmap(StringIota::new, iota -> iota.value)
                .fieldOf("value");

        public static final StreamCodec<RegistryFriendlyByteBuf, StringIota> STREAM_CODEC = ByteBufCodecs
                .stringUtf8(MAX_SIZE)
                .map(StringIota::new, iota -> iota.value)
                .mapStream(buf -> buf);

        @Override
        public MapCodec<StringIota> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, StringIota> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public int color() {
            //A bit darker purple than ListIota
            return 0xff_990099;
        }
    };
}
