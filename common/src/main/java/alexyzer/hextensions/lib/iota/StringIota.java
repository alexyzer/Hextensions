package alexyzer.hextensions.lib.iota;

import alexyzer.hextensions.Hextensions;
import alexyzer.hextensions.lib.mishap.MishapStringTooBig;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringIota extends Iota {

    public final static int MAX_SIZE = 4096;
    public final static ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("string");

    public @NotNull String value;

    public StringIota(@NotNull String value) throws MishapStringTooBig {
        super(() -> TYPE);
        if (value.length() > MAX_SIZE) throw new MishapStringTooBig();
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
        return Component.literal(String.format("\"%s\"", value)).setStyle(STYLE);
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
            //Teal
            return 0xff_0088cc;
        }
    };

    @Override
    public int size() {
        //1 per new 32 characters
        return (value.length() + 32) / 32;
    }

    static public final Style STYLE = Style.EMPTY.withColor(TYPE.color());

    public static StringIota expect(Iota iota, int reverseIdx) throws MishapInvalidIota {
        if (iota instanceof StringIota stringIota) return stringIota;
        throw MishapInvalidIota.ofType(iota, reverseIdx, RESOURCE_LOCATION.toString());
    }

    public static @Nullable String expectNullable(Iota iota, int reverseIdx) throws MishapInvalidIota {
        return switch (iota) {
            case StringIota stringIota -> stringIota.value;
            case NullIota nullIota -> null;
            default -> throw MishapInvalidIota.ofType(iota, reverseIdx, "hextensions:string_or_null");
        };
    }
}
