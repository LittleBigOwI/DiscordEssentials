package net.philocraft;

import org.bukkit.plugin.java.JavaPlugin;

public final class DiscordEssentials extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Plugin disabled.");
    }

}