package be.florens.eggtab.handlers;

import be.florens.eggtab.EggTab;
import be.florens.eggtab.ItemGroupHandler;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.Set;

import static be.florens.eggtab.EggTab.CONFIG;

public class CreativeGroupHandler extends ItemGroupHandler {

    private static final Set<Item> EXCLUDED_ITEMS = Set.of(Items.AIR, Items.ENCHANTED_BOOK);

    public CreativeGroupHandler() {
        super(() -> FabricItemGroupBuilder.build(
                new Identifier(EggTab.MOD_ID, "creative_group"), () -> new ItemStack(Items.COMMAND_BLOCK)
        ), CreativeGroupHandler::shouldRelocate);
    }

    private static boolean shouldRelocate(Item item) {
        return CONFIG.creativeGroup && item.getGroup() == null && !EXCLUDED_ITEMS.contains(item);
    }
}
