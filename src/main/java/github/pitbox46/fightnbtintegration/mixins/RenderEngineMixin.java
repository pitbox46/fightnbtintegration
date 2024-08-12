package github.pitbox46.fightnbtintegration.mixins;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.events.engine.RenderEngine;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;

import java.util.Map;

/**
 * Fixes rendering of Tetra items. It does this while keeping Tetra as an optional dependency
 */
@Mixin(value = RenderEngine.class, remap = false)
public abstract class RenderEngineMixin {
    @Shadow @Final private Map<Item, RenderItemBase> itemRendererMapByInstance;

    @Inject(at = @At(value = "HEAD"), method = "findMatchingRendererByClass", cancellable = true)
    private void afterFindMatchingRendererByClass(Class<?> clazz, CallbackInfoReturnable<RenderItemBase> cir) {
        switch(clazz.getName()) {
            case "se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem" ->
                    cir.setReturnValue(itemRendererMapByInstance.get(Items.SHIELD));
            case "se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem" ->
                    cir.setReturnValue(itemRendererMapByInstance.get(Items.CROSSBOW));
            case "se.mickelus.tetra.items.modular.impl.bow.ModularBowItem" ->
                    cir.setReturnValue(itemRendererMapByInstance.get(Items.BOW));
        }
    }
}
