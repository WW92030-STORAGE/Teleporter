package teleport.mod.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import teleport.mod.items.Teleporter;

public class ItemInit {
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item TELEPORTERI = new Teleporter("teleporter", 4);
	public static final Item TELEPORTERII = new Teleporter("teleporter2", 6);
	public static final Item TELEPORTERIII = new Teleporter("teleporter3", 8);
	public static final Item TELEPORTERIV = new Teleporter("teleporter4", 10);
	public static final Item TELEPORTERV = new Teleporter("teleporter5", 12);
}
