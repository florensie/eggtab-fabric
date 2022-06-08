package be.florens.eggtab;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EggTabTest implements ModInitializer {

	public static final String MOD_ID = "eggtabtest";

	@Override
	public void onInitialize() {
		ItemGroup testGroup = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "test_group"),
				() -> new ItemStack(Items.END_PORTAL_FRAME));
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "test_food"),
				new Item((new Item.Settings()).group(testGroup).food(FoodComponents.BAKED_POTATO)));
	}
}
