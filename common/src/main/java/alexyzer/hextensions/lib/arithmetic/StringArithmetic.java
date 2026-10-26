package alexyzer.hextensions.lib.arithmetic;

import alexyzer.hextensions.Hextensions;
import alexyzer.hextensions.lib.iota.StringIota;
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic;
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator;
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic;
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary;
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary;
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.BooleanIota;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidOperatorArgs;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiPredicate;

public class StringArithmetic implements Arithmetic {

    public static final StringArithmetic INSTANCE = new StringArithmetic();
    public static final ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("string_ops");

    @Override
    public String arithName() {
        return RESOURCE_LOCATION.getPath();
    }

    public static final Object2ObjectOpenHashMap<HexPattern, Operator> PATTERN_TO_OPERATOR = new Object2ObjectOpenHashMap<>();
    static {
        PATTERN_TO_OPERATOR.put(ADD, Operators.CONCAT);
        PATTERN_TO_OPERATOR.put(ABS, Operators.LENGTH);
        PATTERN_TO_OPERATOR.put(INDEX, Operators.INDEX);
        PATTERN_TO_OPERATOR.put(SLICE, Operators.SLICE);
        PATTERN_TO_OPERATOR.put(REV, Operators.REVERSE);
        PATTERN_TO_OPERATOR.put(INDEX_OF, Operators.INDEX_OF);
        PATTERN_TO_OPERATOR.put(REMOVE, Operators.REMOVE);
        PATTERN_TO_OPERATOR.put(REPLACE, Operators.REPLACE);
        PATTERN_TO_OPERATOR.put(GREATER, Operators.GREATER);
        PATTERN_TO_OPERATOR.put(LESS, Operators.LESS);
        PATTERN_TO_OPERATOR.put(GREATER_EQ, Operators.GREATER_EQ);
        PATTERN_TO_OPERATOR.put(LESS_EQ, Operators.LESS_EQ);
        PATTERN_TO_OPERATOR.trim();
    }

    public static final ObjectSet<HexPattern> OPERATOR_TYPES = PATTERN_TO_OPERATOR.keySet();

    @Override
    public Iterable<HexPattern> opTypes() {
        return OPERATOR_TYPES;
    }

    @Override
    public Operator getOperator(HexPattern pattern) {
        return PATTERN_TO_OPERATOR.get(pattern);
    }

    public static class Operators {

        public static final IotaMultiPredicate ALL_STRINGS = iotas -> {
            for (Iota iota : iotas) if (!(iota instanceof StringIota)) return false;
            return true;
        };

        public static final IotaMultiPredicate STRING_AND_NUMBER = iotas -> {
            var iotasIterator = iotas.iterator();
            return
                iotasIterator.next() instanceof StringIota &&
                iotasIterator.next() instanceof DoubleIota;
        };

        public static final OperatorBinary CONCAT = new OperatorBinary(
                ALL_STRINGS,
                (a, b) -> new StringIota(((StringIota) a).value + ((StringIota) b).value)
        );


        public static final OperatorUnary LENGTH = new OperatorUnary(
                ALL_STRINGS,
                iota -> new DoubleIota(((StringIota) iota).value.length())
        );

        public static final OperatorBinary INDEX = new OperatorBinary(
                STRING_AND_NUMBER,
                (stringIota, indexIota) -> {
                    var string = ((StringIota) stringIota).value;
                    var index = (int) Math.round(((DoubleIota) indexIota).getDouble());
                    var character = string.substring(index, index + 1);
                    return new StringIota(character);
                }
        );

        public static final OperatorBasic SLICE = new OperatorBasic(
                3,
                iotas -> {
                    var iotasIterator = iotas.iterator();
                    return
                        iotasIterator.next() instanceof StringIota &&
                        iotasIterator.next() instanceof DoubleIota &&
                        iotasIterator.next() instanceof DoubleIota;
                }
        ) {
            @Override
            public @NotNull Iterable<Iota> apply(@NotNull Iterable<? extends Iota> iotas, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
                var iotasIterator = iotas.iterator();
                var string = ((StringIota) iotasIterator.next()).value;
                var from = (int) Math.round(((DoubleIota) iotasIterator.next()).getDouble());
                var to = (int) Math.round(((DoubleIota) iotasIterator.next()).getDouble());
                var substring = string.substring(from, to);
                return List.of(new StringIota(substring));
            }
        };

        public static final OperatorUnary REVERSE = new OperatorUnary(
                ALL_STRINGS,
                iota -> new StringIota(new StringBuilder(((StringIota) iota).value).reverse().toString())
        );

        public static final OperatorBinary INDEX_OF = new OperatorBinary(
                ALL_STRINGS,
                (stringIota, queryIota) -> {
                    var string = ((StringIota) stringIota).value;
                    var query = ((StringIota) queryIota).value;
                    var index = string.indexOf(query);
                    return new DoubleIota(index);
                }
        );

        public static final OperatorBinary REMOVE = new OperatorBinary(
                STRING_AND_NUMBER,
                (stringIota, removeIdxIota) -> {
                    var string = ((StringIota) stringIota).value;
                    var removeIdx = (int) Math.round(((DoubleIota) removeIdxIota).getDouble());
                    var result = string.substring(0, removeIdx) + string.substring(removeIdx + 1);
                    return new StringIota(result);
                }
        );

        public static final OperatorBasic REPLACE = new OperatorBasic(
                3,
                iotas -> {
                    var iotasIterator = iotas.iterator();
                    return
                        iotasIterator.next() instanceof StringIota &&
                        iotasIterator.next() instanceof DoubleIota &&
                        iotasIterator.next() instanceof StringIota;
                }
        ) {
            @Override
            public @NotNull Iterable<Iota> apply(@NotNull Iterable<? extends Iota> iotas, @NotNull CastingEnvironment castingEnvironment) throws Mishap {
                var iotasIterator = iotas.iterator();

                var string = ((StringIota) iotasIterator.next()).value;
                var replaceIdx = (int) Math.round(((DoubleIota) iotasIterator.next()).getDouble());

                var replaceWithIota = (StringIota) iotasIterator.next();
                var replaceWith = replaceWithIota.value;
                if (replaceWith.length() != 1)
                    throw new MishapInvalidOperatorArgs(List.of(replaceWithIota));

                var result = new StringBuilder(string).replace(replaceIdx, replaceIdx + 1, replaceWith).toString();
                return List.of(new StringIota(result));
            }
        };

        public static class OperatorStringCompare extends OperatorBinary {
            public OperatorStringCompare(BiPredicate<String, String> comparator) {
                super(ALL_STRINGS, (iotaA, iotaB) -> {
                    var stringA = ((StringIota) iotaA).value;
                    var stringB = ((StringIota) iotaB).value;
                    var result = comparator.test(stringA, stringB);
                    return new BooleanIota(result);
                });
            }
        }

        public static final OperatorBinary GREATER    = new OperatorStringCompare((a, b) -> a.compareTo(b) >  0);
        public static final OperatorBinary LESS       = new OperatorStringCompare((a, b) -> a.compareTo(b) <  0);
        public static final OperatorBinary GREATER_EQ = new OperatorStringCompare((a, b) -> a.compareTo(b) >= 0);
        public static final OperatorBinary LESS_EQ    = new OperatorStringCompare((a, b) -> a.compareTo(b) <= 0);
    }
}