package be.florens.eggtab;

import be.florens.eggtab.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Item groups are accessible through {@link ItemGroupHandler#getItemGroup()} ({@link EggTab#handlers})<br>
 * The enchanted books group is available as {@link EggTab#BOOK_GROUP}
 * </p>
 *
 * <p>All item groups can be disabled and can be <code>null</code></p>
 */
public class EggTab implements ClientModInitializer {

	public static final String MOD_ID = "eggtab";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final ModConfig CONFIG = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new).getConfig();
	public static @Nullable ItemGroup BOOK_GROUP;
	public static final List<ItemGroupHandler> handlers = Arrays.asList(
			new ItemGroupHandler(() -> FabricItemGroupBuilder.build(
					new Identifier(MOD_ID, "egg_group"), () -> new ItemStack(Items.CREEPER_SPAWN_EGG)
			), item -> CONFIG.eggsGroup && item instanceof SpawnEggItem),
			new ItemGroupHandler(ItemGroup.FOOD, item -> CONFIG.foodGroup && item.isFood())
	);

	@Override
	public void onInitializeClient() {
		// Iterate all registered items
		LOGGER.info("[Egg Tab] Moving already registered items");
		Registry.ITEM.forEach(EggTab::callHandlers);

		// Log when finished
		handlers.forEach(handler -> {
			String msg = handler.getLogMessage();
			if (msg != null) {
				LOGGER.info("[Egg Tab] " + msg);
			}
		});

		// Register callback for items registered after our init
		LOGGER.info("[Egg Tab] Now listening for new items");
		RegistryEntryAddedCallback.event(Registry.ITEM).register((rawId, id, item) -> callHandlers(item));

		// Enchanted Books group
		if (CONFIG.booksGroup) {
			LOGGER.info("[Egg Tab] Moving enchanted books");

			// Remove enchantments from all groups
			Arrays.stream(ItemGroup.GROUPS).forEach(ItemGroup::setEnchantments);

			BOOK_GROUP = FabricItemGroupBuilder.build(
					new Identifier(MOD_ID, "book_group"),
					() -> new ItemStack(Items.ENCHANTED_BOOK)
			).setEnchantments(EnchantmentTarget.values()); // Add all enchantments to new group
		}
	}

	private static void callHandlers(Item item) {
		handlers.forEach(handler -> handler.handle(item));
	}
}
