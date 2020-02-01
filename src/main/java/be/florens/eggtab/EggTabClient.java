package be.florens.eggtab;

import be.florens.eggtab.item.DummyEggIcon;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

/**
 * Client only class for setting up {@link net.minecraft.client.color.item.ItemColorProvider}
 */
@SuppressWarnings("unused")
public class EggTabClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		if(EggTab.CONFIG.eggsGroup) {
			ColorProviderRegistry.ITEM.register((stack, tintIndex) -> DummyEggIcon.ItemColor.getColor(tintIndex), EggTab.EGG_GROUP_ICON);
		}
	}
}
