package be.florens.eggtab;

import be.florens.eggtab.mixin.extensions.ItemGroupExtensions;
import be.florens.eggtab.mixin.mixins.ItemAccessor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ItemGroupHandler {

	private final Predicate<Item> predicate;
	private ItemGroup itemGroup;
	private Supplier<ItemGroup> itemGroupSupplier;
	private int movedCount;

	protected ItemGroupHandler(ItemGroup itemGroup, Predicate<Item> predicate) {
		this.itemGroup = itemGroup;
		this.predicate = predicate;
	}

	protected ItemGroupHandler(Supplier<ItemGroup> itemGroupSupplier, Predicate<Item> predicate) {
		this.itemGroupSupplier = itemGroupSupplier;
		this.predicate = predicate;
	}

	public void handle(Item item) {
		if (this.predicate.test(item)) {
			ItemGroup oldGroup = item.getGroup();
			ItemGroup newGroup = this.getOrCreateItemGroup();

			if (!Objects.equals(oldGroup, newGroup)) {
				((ItemAccessor) item).setGroup(newGroup);
				this.movedCount++;
				if (EggTab.CONFIG.removeEmptyGroups) {
					hideGroupIfEmpty(oldGroup);
				}
			}
		}
	}

	private static void hideGroupIfEmpty(ItemGroup itemGroup) {
		// Return early if the itemGroup is not empty
		for (Item item : Registry.ITEM) {
			if (Objects.equals(item.getGroup(), itemGroup)) {
				return;
			}
		}

		((ItemGroupExtensions) itemGroup).eggtab$hideFromCreativeInventory();
		EggTab.LOGGER.info("[Egg Tab] Removed an empty item group: " + itemGroup.getName());
	}

	public @Nullable String getLogMessage() {
		if (itemGroup != null) {
			return "Moved " + movedCount + " items to " + itemGroup.getName();
		}
		return null;
	}

	/**
	 * Get the item group for the handler, null if it hasn't been created yet.
	 *
	 * @return the {@link ItemGroup} for this handler or <code>null</code>
	 */
	public @Nullable ItemGroup getItemGroup() {
		return this.itemGroup;
	}

	/**
	 * Get the item group for the handler, create it if it doesn't exist yet.
	 * Note that this also adds it to the creative menu
	 *
	 * @return the {@link ItemGroup} for this handler
	 */
	public ItemGroup getOrCreateItemGroup() {
		if (this.itemGroup == null) {
			this.itemGroup = this.itemGroupSupplier.get();
		}
		return this.itemGroup;
	}
}
