package github.pitbox46.fightnbtintegration.mixins;

import github.pitbox46.fightnbtintegration.Config;
import maninhouse.epicfight.capabilities.ModCapabilities;
import maninhouse.epicfight.capabilities.item.CapabilityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = ModCapabilities.class, remap = false)
public class ModCapabilitiesMixin {
    @Inject(at = @At(value = "HEAD"), method = "stackCapabilityGetter(Lnet/minecraft/item/ItemStack;)Lmaninhouse/epicfight/capabilities/item/CapabilityItem;", cancellable = true)
    private static void onStackCapabilityGetter(ItemStack stack, CallbackInfoReturnable<CapabilityItem> cir) {
        if(stack.isEmpty()) cir.setReturnValue(null);
        CapabilityItem cap = stack.getCapability(ModCapabilities.CAPABILITY_ITEM, null).orElse(null);
        if(cap == null && stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            for(String key: tag.keySet()) {
                if(Config.JSON_MAP.containsKey(key)) {
                    String value = tag.getString(key);
                    if(Config.JSON_MAP.get(key).containsKey(value)) {
                        cir.setReturnValue(Config.DICTIONARY.get(Config.JSON_MAP.get(key).get(value)).apply(stack));
                    }
                }
            }
        }
    }
}
