package be.florens.eggtab;

import be.florens.eggtab.config.ModConfig;
import be.florens.eggtab.mixin.ItemAccessor;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The mod needs to be installed on the server too because it adds an item for the tab icon
 */
@SuppressWarnings("unused")
public class EggTab implements ClientModInitializer {
	public static final String MOD_ID = "eggtab";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final ModConfig CONFIG = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new).getConfig();

	// Icon should always be registered, can't join server without it
	public static ItemGroup EGG_GROUP;
	public static ItemGroup BOOK_GROUP;

	@Override
	public void onInitializeClient() {
		// Spawn Eggs group
		if (CONFIG.eggsGroup) {
			EGG_GROUP = FabricItemGroupBuilder.build(
				new Identifier(MOD_ID, "egg_group"),
				() -> new ItemStack(Items.CREEPER_SPAWN_EGG)
			);

			LOGGER.info("[Egg Tab] Moving spawn eggs");

			// Do all the spawn eggs that have already been registered first
			for (SpawnEggItem spawnEggItem : SpawnEggItem.getAll()) {
				changeGroupIfEgg(spawnEggItem);
			}

			LOGGER.info("[Egg Tab] Now listening for new eggs");

			// Make sure we get any eggs that get registered in the future
			RegistryEntryAddedCallback.event(Registry.ITEM).register((rawId, id, item) -> changeGroupIfEgg(item));
		}

		// Enchanted Books group
		if (CONFIG.booksGroup) {
			LOGGER.info("[Egg Tab] Moving enchanted books");
			BOOK_GROUP = FabricItemGroupBuilder.build(
					new Identifier(MOD_ID, "book_group"),
					() -> new ItemStack(Items.ENCHANTED_BOOK)
			).setEnchantments(EnchantmentTarget.values()); // Add all enchantments to new group

			// Remove enchantments from Tools and Combat groups
			ItemGroup.TOOLS.setEnchantments();
			ItemGroup.COMBAT.setEnchantments();
		}
	}

	/**
	 * Change the group of an {@link Item} if it is a {@link SpawnEggItem}.
	 *
	 * @param item item to check and change group of
	 */
	private static void changeGroupIfEgg(Item item) {
		if (item instanceof SpawnEggItem) {
			((ItemAccessor) item).setGroup(EGG_GROUP);
			LOGGER.info("[Egg Tab] Egged: " + Registry.ITEM.getId(item).toString());
		}
	}
}
