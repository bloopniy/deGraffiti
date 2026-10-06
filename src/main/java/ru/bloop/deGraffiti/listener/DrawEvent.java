package ru.bloop.deGraffiti.listener;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.w3c.dom.CDATASection;
import ru.bloop.deGraffiti.DeGraffiti;
import ru.bloop.deGraffiti.spraycan.SprayCan;

import java.util.List;
import java.util.Objects;


public class DrawEvent implements Listener {

    private final Plugin plugin;

    public DrawEvent(Plugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        final ItemStack playersItem = event.getItem();
        final Location interactLoc = event.getInteractionPoint();

        if (playersItem == null || interactLoc == null || event.getClickedBlock() == null || event.getPlayer().isSneaking()) return;
        if (!SprayCan.isSprayCan(playersItem)) return;
        if (SprayCan.getPaintAmount(playersItem) <= 0) return;

        final World world = event.getPlayer().getWorld();
        interactLoc.setY(interactLoc.getY() - 0.2d);

        world.spawn(interactLoc, TextDisplay.class, (display) -> {
            display.setSeeThrough(false);
            display.setShadowed(false);
            display.setMetadata(DeGraffiti.NAMESPACE, new FixedMetadataValue(plugin, "graffiti"));

            display.text(MiniMessage.miniMessage().deserialize("<red>*"));
            display.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));

            display.teleport(display.getLocation().add(calculateLocationOffset(event.getBlockFace())));
            display.setRotation(calculateYaw(event.getBlockFace()), 0);
        });
        SprayCan.setPaintAmount(playersItem, SprayCan.getPaintAmount(playersItem) - 1);
    }

    @EventHandler
    public void showMenuEvent(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack playersItem = event.getItem();
        if (!player.isSneaking()) return;
        if (playersItem == null) return;
        if (!SprayCan.isSprayCan(playersItem)) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        final Inventory sprayCanInv = Bukkit.createInventory(null, InventoryType.HOPPER, MiniMessage.miniMessage().deserialize("<red><b>УРОВЕНЬ КРАСКИ В БАЛОНЕ"));
        final int oneSlotWeight = SprayCan.MAX_PAINT_LEVEL / sprayCanInv.getSize();
        final int paintLeft = SprayCan.getPaintAmount(playersItem);

        for (int i = 0; i < sprayCanInv.getSize(); ++i) {
            int currentSlotWeight = oneSlotWeight * (1+i);

            if (currentSlotWeight > paintLeft)
                sprayCanInv.setItem(i, negativeItem(currentSlotWeight));
            else
                sprayCanInv.setItem(i, positiveItem(currentSlotWeight));
        }
        player.openInventory(sprayCanInv);
    }

    private ItemStack positiveItem(int weight) {
        final ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.displayName(MiniMessage.miniMessage().deserialize("<green><b><!i>Есть"));
        meta.lore(List.of(MiniMessage.miniMessage().deserialize("<!i><gray>Краски: " + weight)));

        item.setItemMeta(meta);
        return item;
    }
    private ItemStack negativeItem(int weight) {
        final ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.displayName(MiniMessage.miniMessage().deserialize("<red><b><!i>Нет"));
        meta.lore(List.of(MiniMessage.miniMessage().deserialize("<!i><gray>Краски: " + weight)));

        item.setItemMeta(meta);
        return item;
    }

    // навайбкодил, не судите strogo
    private float calculateYaw(BlockFace face) {
        return switch (face) {
            case BlockFace.NORTH -> 180f;
            case BlockFace.WEST -> 90f;
            case BlockFace.EAST -> -90f;
            default -> 0f;
        };
    }

    private Vector calculateLocationOffset(BlockFace face) {
        final Vector vector = new Vector();
        switch (face) {
            case BlockFace.NORTH -> vector.setZ(-0.01);
            case BlockFace.WEST -> vector.setX(-0.01);
        }
        return vector;
    }
}
