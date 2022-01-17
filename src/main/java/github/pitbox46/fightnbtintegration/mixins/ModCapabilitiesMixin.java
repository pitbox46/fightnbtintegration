package github.pitbox46.fightnbtintegration.mixins;

import github.pitbox46.fightnbtintegration.Config;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.capabilities.ModCapabilities;
import yesman.epicfight.capabilities.item.CapabilityItem;

@Mixin(value = ModCapabilities.class, remap = false)
public class ModCapabilitiesMixin {
    @Inject(at = @At(value = "HEAD"), method = "getItemStackCapability", cancellable = true)
    private static void onGetItemStackCapability(ItemStack stack, CallbackInfoReturnable<CapabilityItem> cir) {
        if(stack.isEmpty()) cir.setReturnValue(CapabilityItem.EMPTY);
        CapabilityItem cap = stack.getCapability(ModCapabilities.CAPABILITY_ITEM, null).orElse(CapabilityItem.EMPTY);
        if(cap == CapabilityItem.EMPTY) {
            cir.setReturnValue(Config.findWeaponByNBT(stack));
        }
    }
}