package be.florens.eggtab.config;

import be.florens.eggtab.EggTab;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = EggTab.MOD_ID)
@Config.Gui.Background("minecraft:textures/block/bookshelf.png")
public class ModConfig implements ConfigData {

	@ConfigEntry.Gui.RequiresRestart
	public boolean arrowsGroup = true;

	@ConfigEntry.Gui.RequiresRestart
	public boolean potionsGroup = true;

	@ConfigEntry.Gui.RequiresRestart
	public boolean eggsGroup = true;

	@ConfigEntry.Gui.RequiresRestart
	public boolean musicGroup = true;

	@ConfigEntry.Gui.RequiresRestart
	public boolean booksGroup = true;

	@ConfigEntry.Gui.RequiresRestart
	public boolean foodGroup = false;

	@ConfigEntry.Gui.RequiresRestart
	public boolean removeEmptyGroups = false;
}
