package github.pitbox46.fightnbtintegration;

import maninhouse.epicfight.capabilities.item.SwordCapability;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.ToolType;

public class DummyItem extends TieredItem {
    public DummyItem(ItemStack stack, ToolType toolType) {
        super(new DummyItemTier(stack.getItem().getHarvestLevel(stack, stack.getToolTypes().stream().findFirst().orElse(toolType), null, null)), new Item.Properties());
    }

    public static class DummyItemTier implements IItemTier {
        public final int harvestLevel;
        public DummyItemTier(int harvestLevel) {
            this.harvestLevel = harvestLevel;
        }

        @Override
        public int getMaxUses() {
            return 0;
        }

        @Override
        public float getEfficiency() {
            return 0;
        }

        @Override
        public float getAttackDamage() {
            return 0;
        }

        @Override
        public int getHarvestLevel() {
            return harvestLevel;
        }

        @Override
        public int getEnchantability() {
            return 0;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return null;
        }
    }
}
