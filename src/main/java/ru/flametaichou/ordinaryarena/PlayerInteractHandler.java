package ru.flametaichou.ordinaryarena;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class PlayerInteractHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onInteract(LivingDeathEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            try {
                OrdinaryArenaBase.arenaService.leaveArena(player, false);
            } catch (Exception e) {
                // do nothing
            }
        }
    }
}
