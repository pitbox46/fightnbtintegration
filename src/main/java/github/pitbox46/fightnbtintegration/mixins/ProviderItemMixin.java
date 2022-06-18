package github.pitbox46.fightnbtintegration.mixins;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.provider.ProviderItem;

import java.util.Map;
import java.util.function.Function;

@Mixin(value = ProviderItem.class, remap = false)
public interface ProviderItemMixin {
    @Accessor
    static Map<Item, CapabilityItem> getCAPABILITIES() {
        throw new RuntimeException();
    }
}
