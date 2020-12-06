package be.florens.eggtab.mixin.mixins;

import be.florens.eggtab.mixin.extensions.ItemGroupExtensions;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.*;

@Mixin(ItemGroup.class)
public class MixinItemGroup implements ItemGroupExtensions {

	@Shadow @Final @Mutable public static ItemGroup[] GROUPS;

	@Shadow @Final @Mutable private int index;

	@Unique
	@Override
	public void eggtab$hideFromCreativeInventory() {
		ItemGroup[] tempGroups = new ItemGroup[GROUPS.length - 1];
		boolean removed = false;

		for (ItemGroup group : GROUPS) {
			//noinspection EqualsBetweenInconvertibleTypes
			if (!group.equals(this) || removed) {

				if (removed) {
					((ItemGroupExtensions) group).eggtab$decrementIndex();
				}
				tempGroups[group.getIndex()] = group;
			} else {
				removed = true;
			}
		}

		if (removed) {
			GROUPS = tempGroups;
		}
	}

	@Unique
	@Override
	public void eggtab$decrementIndex() {
		this.index--;
	}
}
