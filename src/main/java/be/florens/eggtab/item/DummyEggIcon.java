package be.florens.eggtab.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import be.florens.eggtab.util.Color;
import net.minecraft.world.explosion.Explosion;

public class DummyEggIcon extends Item {
	public DummyEggIcon() {
		super(new Item.Settings());
	}

	public static class ItemColor {
		private static Color primaryColor = new Color(0, .7f, 1);
		private static Color secondaryColor = new Color(.5f, .7f, 1);
		private static final float CYCLE_SPEED = .1f; // Amount to change hue per second
		private static long lastFrameMillis = System.currentTimeMillis();

		/**
		 * Get the current color for the given layer.
		 *
		 * @param index the layer for the spawn egg model (0=base, 1=overlay)
		 * @return the RGB value
		 */
		@Environment(EnvType.CLIENT)
		public static int getColor(int index) {
			// Get the right hue, untied from framerate
			long currentFrameMillis = System.currentTimeMillis();
			float step = CYCLE_SPEED/1000 * (currentFrameMillis - lastFrameMillis);
			lastFrameMillis = currentFrameMillis;

			// Cycle the colors
			primaryColor.cycleHue(step);
			secondaryColor.cycleHue(step);

			// Return the right color accent in RGB
			if(index == 0) {
				return primaryColor.toRGB();
			}
			return secondaryColor.toRGB();
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		// Fucking around with cheaters/curious people
		if(user.isCreative()) {
			user.setOnFireFor(5);
			user.getStackInHand(hand).decrement(1);
		}
		world.createExplosion(null, user.x, user.y + 1.7, user.z, 3, Explosion.DestructionType.BREAK);

		//noinspection unchecked,rawtypes
		return new TypedActionResult(ActionResult.SUCCESS, user.getStackInHand(hand));
	}
}
