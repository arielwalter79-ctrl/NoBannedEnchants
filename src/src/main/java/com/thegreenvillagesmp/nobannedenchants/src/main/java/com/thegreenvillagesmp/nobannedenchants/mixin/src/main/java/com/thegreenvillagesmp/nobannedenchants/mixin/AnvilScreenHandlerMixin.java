package com.thegreenvillagesmp.nobannedenchants.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin {

    @Shadow
    private ItemStack output;

    @Inject(method = "updateResult", at = @At("TAIL"))
    private void noBannedEnchants$blockBannedEnchantments(CallbackInfo ci) {
        if (output == null || output.isEmpty()) {
            return;
        }

        ItemEnchantmentsComponent enchantments =
                output.getOrDefault(
                        DataComponentTypes.ENCHANTMENTS,
                        ItemEnchantmentsComponent.DEFAULT
                );

        for (RegistryEntry<Enchantment> enchantment : enchantments.getEnchantments()) {
            RegistryKey<Enchantment> key = enchantment.getKey().orElse(null);

            if (key == null) {
                continue;
            }

            String id = key.getValue().toString();

            if (id.equals("minecraft:thorns")
                    || id.equals("minecraft:density")
                    || id.equals("minecraft:breach")) {
                output = ItemStack.EMPTY;
                return;
            }
        }
    }
}
