package be.florens.eggtab;


import be.florens.eggtab.extension.CreativeModeTabExtensions;
import be.florens.eggtab.mixin.common.ItemAccessor;
import me.shedaniel.architectury.registry.Registries;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CreativeTabHandler {

	private final Predicate<Item> predicate;
	private CreativeModeTab itemGroup;
	private Supplier<CreativeModeTab> itemGroupSupplier;
	private int movedCount;

	/**
	 * Create an {@link CreativeTabHandler} with an existing creative tab.
	 *
	 * @param itemGroup The destination creative tab
	 * @param condition Whether an item should added to the item group
	 */
	public CreativeTabHandler(CreativeModeTab itemGroup, Predicate<Item> condition) {
		this.itemGroup = itemGroup;
		this.predicate = condition;
	}

	/**
	 * Create an {@link CreativeTabHandler} with the creative tab as a predicate.
	 * Useful to prevent creating a tab until necessary.
	 *
	 * @param itemGroupSupplier The supplier for the destination creative tab
	 * @param condition Whether an item should added to the item group
	 */
	public CreativeTabHandler(Supplier<CreativeModeTab> itemGroupSupplier, Predicate<Item> condition) {
		this.itemGroupSupplier = itemGroupSupplier;
		this.predicate = condition;
	}

	public void handle(Item item) {
		if (this.predicate.test(item)) {
			CreativeModeTab oldGroup = item.getItemCategory();
			CreativeModeTab newGroup = this.getOrCreateItemGroup();

			if (!Objects.equals(oldGroup, newGroup)) {
				((ItemAccessor) item).setCategory(newGroup);
				this.movedCount++;
				if (EggTab.CONFIG.removeEmptyGroups) {
					hideGroupIfEmpty(oldGroup);
				}
			}
		}
	}

	private static void hideGroupIfEmpty(CreativeModeTab itemGroup) {
		// Return early if the itemGroup is not empty
		// FIXME: lazy/deferred?
		for (Item item : Registries.get(EggTab.MOD_ID).get(Registry.ITEM_REGISTRY)) {
			if (Objects.equals(item.getItemCategory(), itemGroup)) {
				return;
			}
		}

		((CreativeModeTabExtensions) itemGroup).eggtab$hideFromCreativeInventory();
		EggTab.LOGGER.info("[Egg Tab] Removed an empty item group: " + itemGroup.getDisplayName().getString());
	}

	public @Nullable String getLogMessage() {
		if (this.itemGroup != null) {
			return "Moved " + this.movedCount + " items to " + this.itemGroup.getDisplayName().getString();
		}
		return null;
	}

	/**
	 * Get the item group for the handler, null if it hasn't been created yet.
	 *
	 * @return the {@link CreativeModeTab} for this handler or <code>null</code>
	 */
	public @Nullable CreativeModeTab getItemGroup() {
		return this.itemGroup;
	}

	/**
	 * Get the item group for the handler, create it if it doesn't exist yet.
	 * Note that this also adds it to the creative menu
	 *
	 * @return the {@link CreativeModeTab} for this handler
	 */
	public CreativeModeTab getOrCreateItemGroup() {
		if (this.itemGroup == null) {
			this.itemGroup = this.itemGroupSupplier.get();
		}
		return this.itemGroup;
	}
}
