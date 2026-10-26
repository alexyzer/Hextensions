package alexyzer.hextensions.lib.arithmetic;

import alexyzer.hextensions.Hextensions;
import alexyzer.hextensions.lib.iota.StringIota;
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic;
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator;
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary;
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.BinaryOperator;

public class StringArithmetic implements Arithmetic {

    public static final StringArithmetic INSTANCE = new StringArithmetic();
    public static final ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("string_ops");

    @Override
    public String arithName() {
        return RESOURCE_LOCATION.getPath();
    }

    @Override
    public Iterable<HexPattern> opTypes() {
        return List.of(
            HexActions.ADD.value().prototype()
        );
    }

    @Override
    public Operator getOperator(HexPattern pattern) {
        if (pattern.equals(ADD)) return Operators.ConcatOperator.INSTANCE;
        return null;
    }

    public static class Operators {
        public static class ConcatOperator extends OperatorBinary {

            public static final ConcatOperator INSTANCE = new ConcatOperator(
                    iotas -> {
                        for (Iota iota : iotas)
                            if (!(iota instanceof StringIota)) return false;
                        return true;
                    },
                    (a, b) -> new StringIota(((StringIota) a).value + ((StringIota) b).value)
            );

            public ConcatOperator(IotaMultiPredicate accepts, BinaryOperator<Iota> inner) {
                super(accepts, inner);
            }
        }
    }
}