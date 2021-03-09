package be.florens.eggtab;

import be.florens.eggtab.config.ModConfig;
import me.shedaniel.architectury.registry.CreativeTabs;
import me.shedaniel.architectury.registry.Registries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Item groups are accessible through {@link EggTab#HANDLERS} and {@link CreativeTabHandler#getItemGroup()}<br>
 * The enchanted books group is available as {@link EggTab#BOOK_GROUP}
 * </p>
 *
 * <p>All item groups can be disabled and can be <code>null</code></p>
 */
public class EggTab {

	public static final String MOD_ID = "eggtab";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	// TODO: config stub
	// public static final ModConfig CONFIG = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new).getConfig();
	public static final ModConfig CONFIG = new ModConfig();

	public static final List<CreativeTabHandler> HANDLERS = Arrays.asList(
			new CreativeTabHandler(() -> CreativeTabs.create(
					new ResourceLocation(EggTab.MOD_ID, "egg_group"), () -> new ItemStack(Items.CREEPER_SPAWN_EGG)
			), item -> CONFIG.eggsGroup && item instanceof SpawnEggItem),
			new CreativeTabHandler(CreativeModeTab.TAB_FOOD, item -> CONFIG.foodGroup && item.isEdible())
	);

	public static @Nullable CreativeModeTab BOOK_GROUP;

	public static void init() {
		// Iterate all registered items
		// FIXME: deferred/lazy?
		EggTab.LOGGER.info("[Egg Tab] Moving already registered items");
		Registries.get(MOD_ID).get(Registry.ITEM_REGISTRY).forEach(EggTab::callHandlers);

		// Log when finished
		HANDLERS.forEach(handler -> {
			String msg = handler.getLogMessage();
			if (msg != null) {
				LOGGER.info("[Egg Tab] " + msg);
			}
		});

		// Enchanted Books group
		if (CONFIG.booksGroup) {
			EggTab.LOGGER.info("[Egg Tab] Moving enchanted books");

			// Remove enchantments from all groups
			Arrays.stream(CreativeModeTab.TABS).forEach(CreativeModeTab::setEnchantmentCategories);

			BOOK_GROUP = CreativeTabs.create(
					new ResourceLocation(EggTab.MOD_ID, "book_group"),
					() -> new ItemStack(Items.ENCHANTED_BOOK)
			).setEnchantmentCategories(EnchantmentCategory.values()); // Add all enchantments to new group
		}
	}

	public static void callHandlers(Item item) {
		HANDLERS.forEach(handler -> handler.handle(item));
	}
}
