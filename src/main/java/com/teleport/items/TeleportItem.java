package com.teleport.items;

import com.ibm.icu.text.MessagePattern;
import com.teleport.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.HashSet;

public class TeleportItem extends Item {
    public static final boolean DEBUG = true;
    double x, y, z, theta, step, distance, recharge;

    public TeleportItem(Properties properties, double dist) {
        super(properties);
        step = 1 / 200.0;
        distance = dist;
        recharge = 5;
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }

    public boolean isActive(Entity e) {
        return Reference.active.containsKey(e);
    }

    public int floor(double d) {
        return (int)(Math.floor(d));
    }

    public boolean isFluid(BlockState b) {
        return !b.getMaterial().isSolid();
    }

    @Override
    public void inventoryTick(ItemStack is, Level level, Entity entity, int int1, boolean bool) {
        if (!(entity instanceof Player)) return;
        if (level.isClientSide()) return;
        Player p = (Player)entity;
        if (p.getItemInHand(InteractionHand.MAIN_HAND) != is) return;

        if (Math.random() < 0.1 && DEBUG) System.out.println("TELEPORT IN ACTIVE SLOT");

        calc(level, entity);

        long time = System.nanoTime();
        long stored = time;
        if (isActive(entity)) stored = Reference.active.get(entity);
        if (time - stored >= recharge * 1000000000) Reference.active.remove(entity);

        if (Math.random() < 0.1 && DEBUG) System.out.println("TP " + x + " " + y + " " + z);

        if (!isActive(entity)) level.addParticle(ParticleTypes.END_ROD, x, y, z, 0.0D, 0.0D, 0.0D);
        else level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
    }

    public void calc(Level level, Entity e) {
        x = e.getX();
        y = e.getY();
        z = e.getZ();
        y = Math.round(y);
        if (Math.random() < 0.1 && DEBUG) System.out.println("ENTITY POS " + x + " " + y + " " + z);
        theta = (double) ((1 * (e.getYRot()) + 90 + 720) % 360);
        theta = theta / Reference.DEG;

        double xstep = step * Math.cos(theta);
        double zstep = step * Math.sin(theta);

        for (int i = 0; i < distance / step; i++) {
            x = x + xstep;
            z = z + zstep;

            BlockState aim = level.getBlockState(new BlockPos(floor(x + xstep), floor(y), floor(z + zstep)));
            BlockState head = level.getBlockState(new BlockPos(floor(x + xstep), floor(y + 1), floor(z + zstep)));
            if (!isFluid(aim) || !isFluid(head)) break;
        }

        x -= (0.5 / step) * xstep;
        z -= (0.5 * step) * zstep;

        double ystep = 1.0 / 32.0;
        while (y >= -64) {
            BlockState below = level.getBlockState(new BlockPos(floor(x), floor(y - ystep), floor(z)));
            if (!isFluid(below)) {
                //	System.out.println(below.toString());
                break;
            }
            y -= ystep;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> res = super.use(level, player, hand);
        ItemStack i = res.getObject();
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            if (isActive(player)) return res;
            player.teleportTo(x, y, z);
            Reference.active.put(player, System.nanoTime());
            player.getCooldowns().addCooldown(i.getItem(), (int)(recharge * 20));
        }

        return super.use(level, player, hand);
    }
}