package be.florens.eggtab.config;

import be.florens.eggtab.EggTab;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@SuppressWarnings("unused")
@Config(name = EggTab.MOD_ID)
@Config.Gui.Background("minecraft:textures/block/bookshelf.png")
public class ModConfig implements ConfigData {
	@ConfigEntry.Gui.RequiresRestart
	public boolean eggsGroup = true;

	@ConfigEntry.Gui.RequiresRestart
	public boolean booksGroup = true;

	@ConfigEntry.Gui.RequiresRestart
	public boolean foodGroup = false;

	@ConfigEntry.Gui.RequiresRestart
	public boolean removeEmptyGroups = false;
}
