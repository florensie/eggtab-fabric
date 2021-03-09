package be.florens.eggtab;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EggTab.MOD_ID)
public class EggTabForge {

	public EggTabForge() {
		EventBuses.registerModEventBus(EggTab.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void init(FMLClientSetupEvent event) {
		EggTab.init();
	}
}
