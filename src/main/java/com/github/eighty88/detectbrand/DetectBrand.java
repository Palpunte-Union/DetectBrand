package com.github.eighty88.detectbrand;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DetectBrand extends JavaPlugin implements Listener, PluginMessageListener {

    HashMap<UUID, String> ClientNameMap = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getMessenger().registerIncomingPluginChannel(this, "minecraft:brand", this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "minecraft:brand");

        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        getServer().getMessenger().registerIncomingPluginChannel(this, "LOLIMAHCKER", this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] value) {
        if (channel.equalsIgnoreCase("minecraft:brand") || channel.equalsIgnoreCase("MC|BRAND")) {
            String brand = new String(value, StandardCharsets.UTF_8);
            getLogger().info(player.getName() + "'s Client : " + brand);
            if (!ClientNameMap.containsKey(player.getUniqueId())) {
                ClientNameMap.put(player.getUniqueId(), brand);
            }
        }
    }

    @EventHandler
    public void onRegister(PlayerRegisterChannelEvent event) {
        if (event.getChannel().equals("lunarclient:pm")) {
            ClientNameMap.put(event.getPlayer().getUniqueId(), "Lunar Client");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("§8 §8 §1 §3 §3 §7 §8");

        this.getServer().getScheduler().runTaskLater(this, () -> {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            try {
                dataOutputStream.writeUTF("heartbeat");
            } catch (IOException ignored) {
                return;
            }

            event.getPlayer().sendPluginMessage(DetectBrand.this, "BungeeCord", byteArrayOutputStream.toByteArray());
        }, 20L);

        this.getServer().getScheduler().runTaskLater(this, () -> Bukkit.broadcastMessage(event.getPlayer().getName() + " using " + ClientNameMap.get(event.getPlayer().getUniqueId())), 100L);
    }
}
