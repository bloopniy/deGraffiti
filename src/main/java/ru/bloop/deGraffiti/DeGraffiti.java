package ru.bloop.deGraffiti;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.bloop.deGraffiti.command.GiveCommand;
import ru.bloop.deGraffiti.listener.DrawEvent;
import ru.bloop.deGraffiti.listener.SprayPlaceEvent;

import java.util.Optional;

public final class DeGraffiti extends JavaPlugin {

    public static final String NAMESPACE = "de_graffiti";

    @Override
    public void onEnable() {
        Optional.ofNullable(getCommand("test"))
                .orElseThrow() // impossible
                .setExecutor(new GiveCommand());

        getServer().getPluginManager().registerEvents(new DrawEvent(this), this);
        getServer().getPluginManager().registerEvents(new SprayPlaceEvent(), this);
    }
}
