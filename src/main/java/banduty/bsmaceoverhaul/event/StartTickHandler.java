package banduty.bsmaceoverhaul.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class StartTickHandler implements ServerTickEvents.StartTick {
    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            MaceItem maceItem = null;

            if (player.getMainHandStack().getItem() instanceof MaceItem) {
                maceItem = (MaceItem) player.getMainHandStack().getItem();
            } else if (player.getOffHandStack().getItem() instanceof MaceItem) {
                maceItem = (MaceItem) player.getOffHandStack().getItem();
            }
            if (maceItem != null) {
                int maxInventoryCapacity = player.getInventory().main.size() * 64 +
                        player.getInventory().offHand.size() * 64 - (64 - maceItem.getMaxCount());
                int slownessAmplifier = getSlownessAmplifier(player, maxInventoryCapacity);

                if (slownessAmplifier >= 0) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, slownessAmplifier,
                            false, false, true));
                }
            }
        }
    }

    private static int getSlownessAmplifier(ServerPlayerEntity player, int maxInventoryCapacity) {
        int totalItemCount = 0;

        for (ItemStack itemStack : player.getInventory().main) {
            totalItemCount += itemStack.getCount();
        }

        for (ItemStack itemStack : player.getInventory().offHand) {
            totalItemCount += itemStack.getCount();
        }

        int slownessAmplifier = -1;

        if (totalItemCount >= maxInventoryCapacity) {
            slownessAmplifier = 3;
        } else if (totalItemCount >= maxInventoryCapacity * 3 / 4) {
            slownessAmplifier = 2;
        } else if (totalItemCount >= maxInventoryCapacity / 2) {
            slownessAmplifier = 1;
        } else if (totalItemCount >= maxInventoryCapacity / 4) {
            slownessAmplifier = 0;
        }
        return slownessAmplifier;
    }
}