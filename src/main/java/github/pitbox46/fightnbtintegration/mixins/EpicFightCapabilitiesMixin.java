package github.pitbox46.fightnbtintegration.mixins;

import github.pitbox46.fightnbtintegration.Config;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mixin(value = EpicFightCapabilities.class, remap = false)
public class EpicFightCapabilitiesMixin {
    @Inject(at = @At(value = "HEAD"), method = "getItemStackCapability", cancellable = true)
    private static void onGetItemStackCapability(ItemStack stack, CallbackInfoReturnable<CapabilityItem> cir) {
        if(stack.isEmpty()) {
            cir.setReturnValue(CapabilityItem.EMPTY);
        }
        CapabilityItem cap = Config.findWeaponByNBT(stack);
        if(cap == CapabilityItem.EMPTY) {
            cap = stack.getCapability(EpicFightCapabilities.CAPABILITY_ITEM, null).orElse(CapabilityItem.EMPTY);
        }
        cir.setReturnValue(cap);
    }

    @Inject(at = @At(value = "HEAD"), method = "getItemStackCapabilityOr", cancellable = true)
    private static void onGetItemStackCapabilityOr(ItemStack stack, CapabilityItem defaultCap, CallbackInfoReturnable<CapabilityItem> cir) {
        if(stack.isEmpty()) {
            cir.setReturnValue(defaultCap);
        }
        CapabilityItem cap = Config.findWeaponByNBT(stack);
        if(cap == CapabilityItem.EMPTY) {
            cap = stack.getCapability(EpicFightCapabilities.CAPABILITY_ITEM, null).orElse(CapabilityItem.EMPTY);
        }
        cir.setReturnValue(cap);
    }
}