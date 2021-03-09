package be.florens.eggtab.extension;

public interface CreativeModeTabExtensions {

	/**
	 * Hide this {@link net.minecraft.world.item.CreativeModeTab} from the creative tab by removing it from {@link net.minecraft.world.item.CreativeModeTab#TABS}
	 */
	void eggtab$hideFromCreativeInventory();

	/**
	 * Decrement {@link net.minecraft.world.item.CreativeModeTab#id} by one
	 */
	void eggtab$decrementIndex();
}
