package teleport.mod.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerItemRenderer(Item i, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(i, meta, new ModelResourceLocation(i.getRegistryName(), id));
	}
}
