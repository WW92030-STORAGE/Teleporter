package teleport.mod.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import teleport.mod.Main;
import teleport.mod.init.ItemInit;
import teleport.mod.util.IModel;
import teleport.mod.util.Reference;

public class Teleporter extends Item implements IModel {
	double x;
	double y;
	double z;
	double theta;
	double step;
	double distance;
	double recharge;
	
	public boolean isFluid(IBlockState b) {
		return !b.getMaterial().isSolid();
	}
	
	public int floor(double x) {
		return (int)(Math.floor(x));
	}
	
	public boolean integer(double e) {
		return Math.abs(e - Math.round(e)) < Reference.EPSILON;
	}
	
	public Teleporter(String name, int rho) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.COMBAT);
		
		ItemInit.ITEMS.add(this);
		
		step = 1 / 200.0;
		distance = rho;
		recharge = 5;
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
	@Override
	public boolean hasEffect(ItemStack is) {
		return true;
	}
	
	// time to do the magic
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int slot, boolean b) {
		int x = (int) entity.posX;
		int y = (int) entity.posY;
		int z = (int) entity.posZ;
		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHeldItemMainhand().equals(itemstack)) {
			calc(world, entity);
		}
		
		long time = System.nanoTime();
		long stored = time;
		if (isActive(entity)) stored = Reference.active.get(entity);
		if (time - stored >= recharge * 1000000000) Reference.active.remove(entity);
	}
	
	public boolean isActive(Entity e) {
		return Reference.active.containsKey(e);
	}
	
	public void calc(World world, Entity entity) {
		
		x = entity.posX;
		y = entity.posY;
		z = entity.posZ;
		theta = (double) ((1 * (entity.rotationYaw) + 90 + 720) % 360);
		theta = theta / Reference.DEG;
		
		y = Math.round(y);
		double xstep = step * Math.cos(theta);
		double zstep = step * Math.sin(theta);
		for (int i = 0; i < distance / step; i++) {
			x = x + xstep;
			z = z + zstep;
			
			double offset = 0.5 / step;
			
			IBlockState aim = world.getBlockState(new BlockPos(floor(x + xstep), floor(y), floor(z + zstep)));
			if (!isFluid(aim)) {
			//	System.out.println(aim.toString());
				break;
			}
			IBlockState head = world.getBlockState(new BlockPos(floor(x + xstep), floor(y + 1), floor(z + zstep)));
			if (!isFluid(head)) {
			//	System.out.println(aim.toString());
				break;
			}
		}
		
		double ystep = 1.0 / 32.0;
		while (y >= 0) {
			IBlockState below = world.getBlockState(new BlockPos(floor(x), floor(y - ystep), floor(z)));
			if (!isFluid(below)) {
			//	System.out.println(below.toString());
				break;
			}
			y -= ystep;
		}
		
		// display particle at desired location
		if (!isActive(entity)) world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0, 0, 0);
		else world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ActionResult<ItemStack> res = super.onItemRightClick(world, player, hand);
		ItemStack i = res.getResult();
		
		boolean cheese = false;
		try {
			if (!isActive(player)) {
				if (cheese) player.setPosition(x, y, z);
				else {
					ICommandSender ics = new ICommandSender() {
						@Override
						public String getName() {
							return "";
						}

						@Override
						public boolean canUseCommand(int permission, String command) {
							return true;
						}

						@Override
						public World getEntityWorld() {
							return player.world;
						}

						@Override
						public MinecraftServer getServer() {
							return player.world.getMinecraftServer();
						}

						@Override
						public boolean sendCommandFeedback() {
							return false;
						}

						@Override
						public BlockPos getPosition() {
							return player.getPosition();
						}

						@Override
						public Vec3d getPositionVector() {
							return new Vec3d(player.posX, player.posY, player.posZ);
						}

						@Override
						public Entity getCommandSenderEntity() {
							return player;
						}
					};
					
					String command = "tp @p " + x + " " + y + " " + z;
					player.world.getMinecraftServer().getCommandManager().executeCommand(ics, "effect @e slowness 1 100 true");
					player.world.getMinecraftServer().getCommandManager().executeCommand(ics, command);
					player.world.getMinecraftServer().getCommandManager().executeCommand(ics, "effect @e clear");
					player.world.getMinecraftServer().getCommandManager().executeCommand(ics, "effect @e speed 1 100 true");
					player.world.getMinecraftServer().getCommandManager().executeCommand(ics, "effect @e clear");
				}
				Reference.active.put(player, System.nanoTime());
				player.getCooldownTracker().setCooldown(i.getItem(), (int)(recharge * 20));
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		return res;
	}

}
