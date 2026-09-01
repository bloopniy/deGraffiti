package ru.bloop.deGraffiti.spraycan;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.bloop.deGraffiti.DeGraffiti;

public class SprayCan {
    private static final NamespacedKey PAINT_LEFT_KEY =
            new NamespacedKey(DeGraffiti.NAMESPACE, "paint_left");

    public static ItemStack createItemStack(int paintLevel) {
        final ItemStack sprayCan = new ItemStack(Material.AMETHYST_SHARD);
        final ItemMeta sprayCanMeta = sprayCan.getItemMeta();

        sprayCanMeta.setMaxStackSize(1);
        sprayCanMeta.customName(MiniMessage.miniMessage().deserialize("<yellow><b>БАЛОНЧИК КРАСКИ"));
        sprayCanMeta.getPersistentDataContainer().set(
                PAINT_LEFT_KEY,
                PersistentDataType.INTEGER, paintLevel
        );

        sprayCan.setItemMeta(sprayCanMeta);
        return sprayCan;
    }

    public static boolean isSprayCan(ItemStack item) {
        return item.getPersistentDataContainer().has(
                PAINT_LEFT_KEY
        );
    }

    public static int getPaintAmount(ItemStack item) {
        if (!isSprayCan(item))
            return 0;

        //noinspection DataFlowIssue
        return item.getPersistentDataContainer().get(
                PAINT_LEFT_KEY,
                PersistentDataType.INTEGER
        );
    }


    public static void setPaintAmount(ItemStack item, int amount) {
        if (!isSprayCan(item))
            return;

        final ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(
                PAINT_LEFT_KEY, PersistentDataType.INTEGER, amount
        );

        item.setItemMeta(meta);
    }
}
