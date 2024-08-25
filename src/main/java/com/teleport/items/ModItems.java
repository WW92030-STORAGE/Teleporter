package com.teleport.items;


import com.teleport.util.Reference;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);

    public static final RegistryObject<Item> TELEPORT = ITEMS.register("teleporter",
            () -> new TeleportItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC), 4));
    public static final RegistryObject<Item> TELEPORT2 = ITEMS.register("teleporter2",
            () -> new TeleportItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC), 6));
    public static final RegistryObject<Item> TELEPORT3 = ITEMS.register("teleporter3",
            () -> new TeleportItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC), 8));
    public static final RegistryObject<Item> TELEPORT4 = ITEMS.register("teleporter4",
            () -> new TeleportItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC), 10));
    public static final RegistryObject<Item> TELEPORT5 = ITEMS.register("teleporter5",
            () -> new TeleportItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC), 12));

    public static void register(IEventBus ieb) {
        ITEMS.register(ieb);
    }
}
