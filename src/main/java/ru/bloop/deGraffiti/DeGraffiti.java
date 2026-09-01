package ru.bloop.deGraffiti;

import org.bukkit.plugin.java.JavaPlugin;
import ru.bloop.deGraffiti.command.GiveCommand;
import ru.bloop.deGraffiti.listener.DrawEvent;
import ru.bloop.deGraffiti.listener.SprayPlaceEvent;

public final class DeGraffiti extends JavaPlugin {

    public static final String NAMESPACE = "de_graffiti";

    @Override
    public void onEnable() {
        getCommand("zalupa").setExecutor(new GiveCommand());
        getServer().getPluginManager().registerEvents(new DrawEvent(this), this);
        getServer().getPluginManager().registerEvents(new SprayPlaceEvent(), this);
    }

    @Override
    public void onDisable() {

    }
}
