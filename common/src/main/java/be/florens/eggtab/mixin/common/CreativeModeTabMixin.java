package be.florens.eggtab.mixin.common;

import be.florens.eggtab.extension.CreativeModeTabExtensions;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Iterator;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabMixin implements CreativeModeTabExtensions {

	@Shadow @Final @Mutable public static CreativeModeTab[] TABS;
	@Shadow @Final @Mutable private int id;

	/*@ModifyVariable(method = "fillItemList", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/DefaultedRegistry;iterator()Ljava/util/Iterator;"))
	private Iterator<Item> filterFillItems(Iterator<Item> items) {
		ArrayList<Item> newItems = new ArrayList<>();

		items.forEachRemaining(item -> {
			if (!(item instanceof SpawnEggItem)) {
				newItems.add(item);
			}
		});

		return newItems.iterator();
	}*/

	@Unique
	@Override
	public void eggtab$hideFromCreativeInventory() {
		CreativeModeTab[] tempGroups = new CreativeModeTab[TABS.length - 1];
		boolean removed = false;

		for (CreativeModeTab group : TABS) {
			//noinspection EqualsBetweenInconvertibleTypes
			if (!group.equals(this) || removed) {
				if (removed) {
					((CreativeModeTabExtensions) group).eggtab$decrementIndex();
				}
				tempGroups[group.getId()] = group;
			} else {
				removed = true;
			}
		}

		if (removed) {
			TABS = tempGroups;
		}
	}

	@Unique
	@Override
	public void eggtab$decrementIndex() {
		this.id--;
	}
}
