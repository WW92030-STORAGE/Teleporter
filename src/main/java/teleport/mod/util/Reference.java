package teleport.mod.util;

import java.util.HashMap;

import net.minecraft.entity.Entity;

public class Reference {
	public static final String MODID = "teleport";
	public static final String NAME = "Teleporter";
	public static final String VERSION = "1.0.0";
	public static final String COMMON = "teleport.mod.proxy.CommonProxy";
	public static final String CLIENT = "teleport.mod.proxy.ClientProxy";
	
	public static final double DEG = 180.0 / Math.PI;
	public static final double EPSILON = 0.000000001;
	
	public static HashMap<Entity, Long> active = new HashMap<Entity, Long>();
}
