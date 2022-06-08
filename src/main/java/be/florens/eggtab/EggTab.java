package be.florens.eggtab;

import be.florens.eggtab.config.ModConfig;
import be.florens.eggtab.handlers.CreativeGroupHandler;
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
 * <p>All item groups can be disabled and can be {@code null}</p>
 */
public class EggTab implements ClientModInitializer {

	public static final String MOD_ID = "eggtab";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final ModConfig CONFIG = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new).getConfig();
	public static @Nullable ItemGroup BOOK_GROUP;
	public static final List<ItemGroupHandler> handlers = Arrays.asList(
			new ItemGroupHandler(() -> FabricItemGroupBuilder.build(
					new Identifier(MOD_ID, "arrow_group"), () -> new ItemStack(Items.ARROW)
			), item -> CONFIG.arrowsGroup && item instanceof ArrowItem),
			new ItemGroupHandler(() -> FabricItemGroupBuilder.build(
					new Identifier(MOD_ID, "potion_group"), () -> new ItemStack(Items.POTION)
			), item -> CONFIG.potionsGroup && item instanceof PotionItem),
			new ItemGroupHandler(() -> FabricItemGroupBuilder.build(
					new Identifier(MOD_ID, "egg_group"), () -> new ItemStack(Items.CREEPER_SPAWN_EGG)
			), item -> CONFIG.eggsGroup && item instanceof SpawnEggItem),
			new ItemGroupHandler(() -> FabricItemGroupBuilder.build(
					new Identifier(MOD_ID, "music_group"), () -> new ItemStack(Items.MUSIC_DISC_13)
			), item -> CONFIG.musicGroup && item instanceof MusicDiscItem),
			new ItemGroupHandler(ItemGroup.FOOD, item -> CONFIG.foodGroup && item.isFood()),
			new CreativeGroupHandler()
	);

	@Override
	public void onInitializeClient() {
		// Iterate all registered items
		LOGGER.info("[Egg Tab] Moving already registered items");
		Registry.ITEM.forEach(EggTab::callHandlers);

		// Log when finished
		handlers.forEach(handler -> {
			handler.getLogMessage().ifPresent(m -> LOGGER.info("[Egg Tab] {}", m));
		});

		// Register callback for items registered after our init
		LOGGER.info("[Egg Tab] Now listening for new items");
		RegistryEntryAddedCallback.event(Registry.ITEM).register((rawId, id, item) -> callHandlers(item));

		// Enchanted Books group
		if (CONFIG.booksGroup) {
			LOGGER.info("[Egg Tab] Moving enchanted books");

			// Remove enchantments from all groups
			// FIXME: new itemgroups with enchantments could be added after this
			Arrays.stream(ItemGroup.GROUPS).forEach(ItemGroup::setEnchantments);

			// FIXME: new EnchantmentTargets could be added after this
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
