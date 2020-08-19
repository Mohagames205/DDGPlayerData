package me.mohamedelyousfi.playerdata.provider;

import me.mohamedelyousfi.playerdata.Playerdata;
import me.mohamedelyousfi.playerdata.util.PlayerdataWrapper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class YamlDataProvider implements DataProvider {

    private final Playerdata plugin;

    public YamlDataProvider() {
        plugin = Playerdata.getInstance();
    }

    /**
     * @param data
     * @see DataProvider#save(PlayerdataWrapper)
     */
    @Override
    public void save(PlayerdataWrapper data) {

        // De data moet async geschreven worden naar de file zodat de main thread niet wordt gebrlokkeerd
        new BukkitRunnable() {
            @Override
            public void run() {
                // Een File object maken van de uuid.yml file
                File playerDataFile = new File(plugin.getDataFolder() + "/players/", data.getUniqueId() + ".yml");

                // Als het bestand niet bestaat dan moet deze aangemaakt worden.
                if (!playerDataFile.exists()) {
                    playerDataFile.getParentFile().mkdirs();
                    try {
                        playerDataFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // YamlConfiguration initialisen zodat we de functies van deze class kunnen gebruiken om de values in te stellen
                FileConfiguration playerData = new YamlConfiguration();
                try {
                    playerData.load(playerDataFile);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }

                Location location = data.getLastLocation();

                // LocationMap aanmaken om deze in de config file te zetten
                Map<String, Double> locationMap = new HashMap<String, Double>();
                locationMap.put("x", location.getX());
                locationMap.put("y", location.getY());
                locationMap.put("z", location.getZ());

                // Alle waardes instellen in de config
                playerData.set("player_uuid", data.getUniqueId());
                playerData.set("is_flying", data.isFlying());
                playerData.set("location", locationMap);
                playerData.set("world", location.getWorld().getName());
                playerData.set("gamemode", data.getGameMode());
                playerData.set("total_playtime", data.getOnlineTime());

                // De data schrijven naar het bestand
                try {
                    playerData.save(playerDataFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * @param uuid
     * @return een CompletableFuture met de PlayerDataWrapper als resultaat
     * @see DataProvider#load(String)
     */
    @Override
    public CompletableFuture<PlayerdataWrapper> load(String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            // Een File object maken van de uuid.yml file
            File playerConfigFile = new File(plugin.getDataFolder() + "/players/", uuid + ".yml");

            // Als het bestand niet bestaat, dan kan deze ook niet gereturned worden dus return ik null
            if (!playerConfigFile.exists()) return null;

            FileConfiguration playerData = new YamlConfiguration();
            try {
                playerData.load(playerConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
                return null;
            }

            // We maken hier een mogelijke Player object aan
            Player possiblePlayer = plugin.getServer().getPlayer(UUID.fromString(uuid));

            // Alle data van de file in een variabele stoppen
            boolean isFlying = playerData.getBoolean("is_flying");
            double locationX = playerData.getDouble("location.x");
            double locationY = playerData.getDouble("location.y");
            double locationZ = playerData.getDouble("location.z");
            String worldName = playerData.getString("world");
            World world = plugin.getServer().getWorld(worldName);
            int gamemode = playerData.getInt("gamemode");
            long onlineTime = playerData.getLong("total_playtime");

            Location location = new Location(world, locationX, locationY, locationZ);

            // Een PlayerdataWrapper object returnen met de data.
            return new PlayerdataWrapper(possiblePlayer, uuid, isFlying, location, gamemode, onlineTime);

        });
    }
}
