package github.pitbox46.fightnbtintegration.mixins;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.events.engine.RenderEngine;
import yesman.epicfight.client.renderer.item.RenderItemBase;

/**
 * Fixes rendering of Tetra items. It does this while keeping Tetra as an optional dependency
 */
@Mixin(value = RenderEngine.class, remap = false)
public abstract class RenderEngineMixin {
    @Shadow public abstract RenderItemBase getItemRenderer(Item item);

    @Inject(at = @At(value = "HEAD"), method = "getItemRenderer", cancellable = true)
    private void afterFindMatchingRendererByClass(Item item, CallbackInfoReturnable<RenderItemBase> cir) {
        Class<? extends Item> clazz = item.getClass();
        if(clazz == null) return;
        switch(clazz.getName()) {
            case "se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem": {
                cir.setReturnValue(getItemRenderer(Items.SHIELD));
                return;
            }
            case "se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem": {
                cir.setReturnValue(getItemRenderer(Items.CROSSBOW));
                return;
            }
            case "se.mickelus.tetra.items.modular.impl.bow.ModularBowItem": {
                cir.setReturnValue(getItemRenderer(Items.BOW));
                return;
            }
        }
    }
}
