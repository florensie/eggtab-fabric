package be.florens.eggtab.mixin.extensions;

public interface ItemGroupExtensions {

	/**
	 * Hide this {@link net.minecraft.item.ItemGroup} from the creative tab by removing it from {@link net.minecraft.item.ItemGroup#GROUPS}
	 */
	void eggtab$hideFromCreativeInventory();

	/**
	 * Decrement {@link net.minecraft.item.ItemGroup#index} by one
	 */
	void eggtab$decrementIndex();
}
