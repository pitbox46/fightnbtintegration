package github.pitbox46.fightnbtintegration.mixins;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapabilityPresets;

import java.util.Map;
import java.util.function.Function;

@Mixin(value = WeaponCapabilityPresets.class, remap = false)
public interface WeaponCapabilityPresetsMixin {

    @Accessor
    static Map<String, Function<Item, CapabilityItem.Builder>> getPRESETS() {
        throw new RuntimeException();
    }
}
