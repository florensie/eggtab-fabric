package be.florens.eggtab;

import be.florens.eggtab.item.DummyEggIcon;
import be.florens.eggtab.mixin.MixinItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The mod needs to be installed on the server too because it adds an item for the tab icon
 */
@SuppressWarnings("unused")
public class EggTab implements ModInitializer {
	private static final String MOD_ID = "eggtab";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final DummyEggIcon GROUP_ICON_ITEM = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "egg_group_icon"), new DummyEggIcon());
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
			new Identifier(MOD_ID, "egg_group"),
			() -> new ItemStack(GROUP_ICON_ITEM)
	);

	@Override
	public void onInitialize() {
		LOGGER.info("[Egg Tab] Starting initial egging");

		// Do all the spawn eggs that have already been registered first
		for (SpawnEggItem spawnEggItem : SpawnEggItem.getAll()) {
			changeGroupIfEgg(spawnEggItem);
		}

		LOGGER.info("[Egg Tab] Now listening for new eggs");

		// Make sure we get any eggs that get registered in the future
		RegistryEntryAddedCallback.event(Registry.ITEM).register((rawId, id, item) -> changeGroupIfEgg(item));
	}

	/**
	 * Change the group of an {@link Item} if it is a {@link SpawnEggItem}.
	 *
	 * @param item item to check and change group of
	 */
	private static void changeGroupIfEgg(Item item) {
		if(item instanceof SpawnEggItem) {
			if(Registry.ITEM.getId(item).compareTo(new Identifier(MOD_ID, "egg_group_icon")) == 0) {
				LOGGER.info("[Egg Tab] Ignored tab icon dummy item");
			} else {
				((MixinItem) item).setGroup(ITEM_GROUP);
				LOGGER.info("[Egg Tab] Egged: " + Registry.ITEM.getId(item).toString());
			}
		}
	}
}
