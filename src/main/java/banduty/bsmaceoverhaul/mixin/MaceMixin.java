package banduty.bsmaceoverhaul.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaceItem.class)
public class MaceMixin {
    @Inject(method = "getBonusAttackDamage", at = @At("TAIL"), cancellable = true)
    public void getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource, CallbackInfoReturnable<Float> cir) {
        Entity entity = damageSource.getSource();
        float bonusDamageMultiplier = 0;
        if (entity instanceof PlayerEntity player) {
            for (ItemStack itemStack : player.getInventory().main) {
                bonusDamageMultiplier += itemStack.getCount();
            }
            if (bonusDamageMultiplier <= 1) {
                bonusDamageMultiplier = 2000;
            }
        }
        cir.setReturnValue(cir.getReturnValue() * (bonusDamageMultiplier / 2000));
    }
}
