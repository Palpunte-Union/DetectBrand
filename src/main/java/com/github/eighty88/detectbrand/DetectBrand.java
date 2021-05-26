package com.github.eighty88.detectbrand;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;

public final class DetectBrand extends JavaPlugin implements Listener, PluginMessageListener {
    @Override
    public void onEnable() {
        getServer().getMessenger().registerIncomingPluginChannel(this, "minecraft:brand", this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "minecraft:brand");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] value) {
        if (channel.equalsIgnoreCase("minecraft:brand") || channel.equalsIgnoreCase("MC|BRAND")) {
            String brand = new String(value, StandardCharsets.UTF_8);
            getLogger().info(player.getName() + "'s Client : " + brand);
        }
    }
}
