package ru.bloop.deGraffiti.listener;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import ru.bloop.deGraffiti.DeGraffiti;
import ru.bloop.deGraffiti.spraycan.SprayCan;


public class DrawEvent implements Listener {

    private final Plugin plugin;

    public DrawEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        final ItemStack playersItem = event.getItem();
        final Location interactLoc = event.getInteractionPoint();

        if (playersItem == null || interactLoc == null || event.getClickedBlock() == null) return;
        if (!SprayCan.isSprayCan(playersItem)) return;
        if (SprayCan.getPaintAmount(playersItem) <= 0) return;

        final World world = event.getPlayer().getWorld();
        final Location blockCenter = event.getClickedBlock().getLocation().add(0.5, 0.5, 0.5);
        final Vector normal = event.getBlockFace().getDirection(); // нормаль грани
        interactLoc.setY(interactLoc.getY() - 0.2d);

        world.spawn(interactLoc, TextDisplay.class, (display) -> {
            float yaw = 0;
            float pitch = 0;
            switch (event.getBlockFace()) {
                case BlockFace.SOUTH: yaw = 0f;   break;   // юг
                case BlockFace.NORTH: yaw = 180f; break;   // север
                case BlockFace.WEST:  yaw = 90f;  break;   // запад
                case BlockFace.EAST:  yaw = -90f; break;   // восток
                case BlockFace.UP: // для пола – можно повернуть текстом вниз (pitch = 90)
                case BlockFace.DOWN: // для потолка – вверх (pitch = -90)
                default: break;   // запасной вариант
            }
            display.setSeeThrough(false);
            display.setShadowed(false);
            display.setMetadata(DeGraffiti.NAMESPACE, new FixedMetadataValue(plugin, "graffiti"));

            display.text(MiniMessage.miniMessage().deserialize("<red>*"));
            display.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));

            display.setRotation(yaw, pitch);
        });
        SprayCan.setPaintAmount(playersItem, SprayCan.getPaintAmount(playersItem) - 1);
    }
}
