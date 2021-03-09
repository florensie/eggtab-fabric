package be.florens.eggtab;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.Registry;

public class EggTabFabric implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EggTab.init();

		// Fabric specific
		EggTab.LOGGER.info("[Egg Tab] Now listening for new items");
		RegistryEntryAddedCallback.event(Registry.ITEM).register((rawId, id, item) -> EggTab.callHandlers(item));
	}
}
