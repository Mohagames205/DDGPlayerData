package me.mohamedelyousfi.playerdata.util;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.mohamedelyousfi.playerdata.Playerdata;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerdataWrapper {

    private Player player;

    private String uuid;

    private boolean isFlying;

    private Location lastLocation;

    private int gameMode;

    private long onlineTime;



    public PlayerdataWrapper(Player player, String uuid, boolean isFlying, Location lastLocation, int gameMode, long onlineTime)
    {
        this.player = player;
        this.uuid = uuid;
        this.isFlying = isFlying;
        this.lastLocation = lastLocation;
        this.gameMode = gameMode;
        this.onlineTime = onlineTime;
    }

    public String getUniqueId()
    {
        return uuid;
    }

    public Player getPlayer()
    {
        return player;
    }

    public boolean isFlying()
    {
        return isFlying;
    }

    public Location getLastLocation()
    {
        return lastLocation;
    }

    public int getGameMode()
    {
        return gameMode;
    }

    public long getOnlineTime()
    {
        return onlineTime;
    }

    public static PlayerdataWrapper fromResultset(ResultSet resultSet) throws SQLException {
        String uuid = resultSet.getString("player_uuid");
        boolean isFlying = resultSet.getBoolean("is_flying");

        double posX = resultSet.getDouble("location_x");
        double posY = resultSet.getDouble("location_y");
        double posZ = resultSet.getDouble("location_z");

        World world = Playerdata.getInstance().getServer().getWorld(resultSet.getString("world"));

        Location location = new Location(world, posX, posY, posZ);

        int gamemode = resultSet.getInt("gamemode");
        int onlineTime = resultSet.getInt("total_playtime");

        return new PlayerdataWrapper(null, uuid, isFlying, location, gamemode, onlineTime);
    }

}
