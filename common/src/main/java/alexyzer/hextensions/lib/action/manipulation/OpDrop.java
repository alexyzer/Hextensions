package alexyzer.hextensions.lib.action.manipulation;

import alexyzer.hextensions.Hextensions;
import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpDrop implements SpellAction {

    public final static OpDrop INSTANCE = new OpDrop();
    public final static ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("manipulation/drop");
    public final static HexPattern PATTERN = HexPattern.fromAngleString("qaqqq", HexDir.NORTH_EAST);

    @Override
    public int getArgc() {
        return 1;
    }

    @Override
    public @NotNull Result execute(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env) throws Mishap {
        var dropPos = OperatorUtils.getVec3(args, 0, 1);
        env.assertVecInRange(dropPos);

        var otherHand = env.getOtherHand();
        var foundStack = ItemStack.EMPTY;
        for (CastingEnvironment.HeldItemInfo stackInfo : env.getPrimaryStacks())
            if (otherHand.equals(stackInfo.hand())) {
                foundStack = stackInfo.stack();
                break;
            }

        final /*for lambda*/ var otherHandStack = foundStack;
        return new Result(
                env_ -> {
                    if (!otherHandStack.isEmpty()) {
                        var level = env.getWorld();

                        var otherHandStackCopy = otherHandStack.copy();
                        var itemEntity = new ItemEntity(level, dropPos.x, dropPos.y, dropPos.z, otherHandStackCopy);

                        try {
                            if (level.addFreshEntity(itemEntity)) {
                                //Clear original stack only once ItemEntity confirmed
                                otherHandStack.setCount(0);
                            }
                        } catch (Exception e) {
                            //Revert if something bad happened
                            otherHandStackCopy.setCount(0);
                            itemEntity.discard();

                            throw new RuntimeException(e);
                        }
                    }
                },
                MediaConstants.DUST_UNIT / 8, //Negligible amount
                List.of(
                        //A bit of particles both at "caster" and dropped item
                        new ParticleSpray(env.mishapSprayPos(), Vec3.ZERO, 0.0, 0.1, 5),
                        new ParticleSpray(dropPos, Vec3.ZERO, 0.0, 0.1, 5)
                ),
                1
        );
    }
}
