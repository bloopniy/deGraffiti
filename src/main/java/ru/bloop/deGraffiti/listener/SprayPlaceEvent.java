package ru.bloop.deGraffiti.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import ru.bloop.deGraffiti.spraycan.SprayCan;

public class SprayPlaceEvent implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (SprayCan.isSprayCan(event.getItemInHand()))
            event.setCancelled(true);
    }
}
