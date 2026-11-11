package alexyzer.hextensions.lib.action.manipulation;

import alexyzer.hextensions.Hextensions;
import alexyzer.hextensions.util.Misc;
import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.casting.mishaps.*;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class OpSwap implements SpellAction {

    public final static OpSwap INSTANCE = new OpSwap();
    public final static ResourceLocation RESOURCE_LOCATION = Hextensions.modLoc("manipulation/swap");
    public final static HexPattern PATTERN = HexPattern.fromAngleString("qaqqwqaa", HexDir.NORTH_EAST);

    public static final MutableComponent EXPECTED_SWAP_TARGET = Component.translatable("hexcasting.mishap.hextensions:bad_swap_target");

    @Override
    public int getArgc() {
        /* target, slot -> */
        return 2;
    }

    @Override
    public @NotNull Result execute(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env) throws Mishap {
        Object target;
        Vec3 targetPos;
        Function<Integer, SlotAccess> slotAccessor;
        var targetArg = args.getFirst();
        switch (targetArg) {
            case EntityIota entityIota -> {
                var entity = entityIota.getEntity(env.getWorld());
                if (entity == null) throw MishapEntityNotFound.of(entityIota);
                env.assertEntityInRange(entity);

                target = entity;
                targetPos = entity.position().add(0, entity.getBbHeight()/2, 0);
                slotAccessor = entity::getSlot;
            }
            case Vec3Iota vecIota -> {
                var blockPos = BlockPos.containing(vecIota.getVec3());
                env.assertPosInRangeForEditing(blockPos);
                if (!(env.getWorld().getBlockEntity(blockPos) instanceof Container container))
                    throw new MishapBadBlock(blockPos, EXPECTED_SWAP_TARGET);

                target = container;
                targetPos = blockPos.getCenter();
                slotAccessor = slotIdx -> SlotAccess.forContainer(container, slotIdx);
            }
            default -> throw MishapInvalidIota.ofType(targetArg, 1, "entity_or_vector");
        }

        var slotIdx = getSlotIdxForTarget(target, args);
        var holderSlot = slotAccessor.apply(slotIdx);


        var caster = env.getCastingEntity();
        if (caster == null)
            //Need something to swap items with
            throw new MishapBadCaster();
        var casterPos = caster.position().add(0, caster.getBbHeight()/2, 0);

        var casterSlot = SlotAccess.forEquipmentSlot(
                caster,
                env.getOtherHand() == InteractionHand.OFF_HAND ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND
        );


        var insideOwn = false;
        if (target instanceof Player player)
            if (player.equals(caster)) insideOwn = true;
            else throw new MishapOthersName(player);

        var particleVec = targetPos.subtract(casterPos).normalize().scale(3);
        return new Result(
                env_ -> Misc.swapSlots(casterSlot, holderSlot),
                insideOwn ? 0 : MediaConstants.DUST_UNIT / 8,
                insideOwn ? List.of() : List.of(
                        //A bit of particles flying between caster and target
                        new ParticleSpray(casterPos, particleVec, 1.25, 0.1, 3),
                        new ParticleSpray(targetPos, particleVec.reverse(), 1.25, 0.1, 3)
                ),
                1
        );
    }

    private static int getSlotIdxForTarget(Object target, @NotNull List<? extends Iota> args) {
        int slotIdx = OperatorUtils.getInt(args, 1, 2);
        return switch (target) {
            case Player player -> {
                var argMin = -player.getEnderChestInventory().getContainerSize();
                var argMax = Inventory.INVENTORY_SIZE + Player.ARMOR_SLOTS;

                //Out of range
                if (slotIdx < argMin)
                    throw MishapInvalidIota.of(args.getLast(), 0, "int.between", argMin, argMax);

                //-27 to -1 Ender Chest
                if (slotIdx < 0)
                    yield -(slotIdx + 1) + Player.ENDER_SLOT_OFFSET;

                //0 to 35 Inventory
                if (slotIdx < Inventory.INVENTORY_SIZE)
                    yield slotIdx + Player.CONTENTS_SLOT_INDEX;

                //36 Offhand
                if (slotIdx == Inventory.INVENTORY_SIZE)
                    yield Player.EQUIPMENT_SLOT_OFFSET + EquipmentSlot.OFFHAND.getIndex();

                //37 to 40 Armor
                slotIdx = slotIdx - Inventory.INVENTORY_SIZE - 1;
                if (slotIdx < Player.ARMOR_SLOTS)
                    yield slotIdx + Player.ARMOR_SLOT_OFFSET;

                //Out of range
                throw MishapInvalidIota.of(args.getLast(), 0, "int.between", argMin, argMax);
            }

            case ArmorStand armorStand -> {
                var equipmentSlot = switch (slotIdx) {
                    case 0 -> EquipmentSlot.FEET;
                    case 1 -> EquipmentSlot.LEGS;
                    case 2 -> EquipmentSlot.CHEST;
                    case 3 -> EquipmentSlot.HEAD;
                    default -> {
                        if (armorStand.isShowArms())
                            yield switch (slotIdx) {
                                case -1 -> EquipmentSlot.OFFHAND;
                                case -2 -> EquipmentSlot.MAINHAND;
                                default -> throw MishapInvalidIota.of(args.getLast(), 0, "int.between", -2, 3);
                            };

                        throw MishapInvalidIota.of(args.getLast(), 0, "int.between", 0, 3);
                    }
                };

                if (armorStand.isMarker() || armorStand.isDisabled(equipmentSlot))
                    throw new MishapImmuneEntity(armorStand);

                yield slotIdx + ArmorStand.ARMOR_SLOT_OFFSET;
            }

            case AbstractChestedHorse chestedHorse -> {
                var argMax = chestedHorse.getInventorySize() - 1;
                if (slotIdx < 0 || slotIdx > argMax)
                    throw MishapInvalidIota.of(args.getLast(), 0, "int.between", 0, argMax);

                yield slotIdx + AbstractChestedHorse.CHEST_SLOT_OFFSET;
            }

            case ItemEntity itemEntity -> {
                if (slotIdx != 0) throw MishapInvalidIota.of(args.getLast(), 0, "int.between", 0, 0);

                if (itemEntity.pickupDelay == /*inf*/ 32767)
                    throw new MishapImmuneEntity(itemEntity);

                yield 0;
            }

            case ItemFrame itemFrame -> {
                if (slotIdx != 0) throw MishapInvalidIota.of(args.getLast(), 0, "int.between", 0, 0);

                if (itemFrame.isInvulnerable() || itemFrame.fixed)
                    throw new MishapImmuneEntity(itemFrame);

                yield slotIdx;
            }

            case Container container -> {
                var argMax = container.getContainerSize() - 1;
                if (slotIdx < 0 || slotIdx > argMax)
                    throw MishapInvalidIota.of(args.getLast(), 0, "int.between", 0, argMax);

                yield slotIdx;
            }

            default -> throw new MishapBadEntity(((Entity) target), EXPECTED_SWAP_TARGET);
        };
    }
}
