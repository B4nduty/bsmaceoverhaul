package banduty.bsmaceoverhaul.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaceItem.class)
public class MaceMixin {
    @Inject(method = "getBonusAttackDamage", at = @At("HEAD"), cancellable = true)
    public void getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource, CallbackInfoReturnable<Float> cir) {
        float bonusDamageMultiplier = 0;
        Entity entity = damageSource.getSource();
        if (entity instanceof PlayerEntity attackerPlayerEntity) {
            if (target instanceof PlayerEntity targetPlayerEntity && targetPlayerEntity.getInventory().getArmorStack(3).isOf(Items.TURTLE_HELMET)) {
                cir.setReturnValue(0F);
                return;
            }
            for (ItemStack itemStack : attackerPlayerEntity.getInventory().main) {
                bonusDamageMultiplier += itemStack.getCount();
            }
        }

        cir.setReturnValue(cir.getReturnValue() + (bonusDamageMultiplier / 100));
    }

    @Inject(method = "postDamageEntity", at = @At("HEAD"), cancellable = true)
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfo ci) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
        ci.cancel();
    }
}
