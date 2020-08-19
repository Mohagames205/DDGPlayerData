package me.mohamedelyousfi.playerdata.provider;

import me.mohamedelyousfi.playerdata.Playerdata;
import me.mohamedelyousfi.playerdata.provider.mysql.DataSource;
import me.mohamedelyousfi.playerdata.util.PlayerdataWrapper;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.scheduler.BukkitRunnable;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class SqlDataProvider implements DataProvider {

    private final Connection connection;
    private final Playerdata plugin;

    public SqlDataProvider() throws SQLException {
        connection = DataSource.getConnection();

        plugin = Playerdata.getInstance();
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS playerdata(data_id int NOT NULL AUTO_INCREMENT, player_uuid VARCHAR(36), is_flying BOOLEAN, location_x DOUBLE, location_y DOUBLE, location_z DOUBLE, world VARCHAR(255), gamemode int, total_playtime int, primary key (data_id))");

    }

    @Override
    public void save(PlayerdataWrapper data) {
        new BukkitRunnable()  {
            @Override
            public void run() {
                try {
                    Location location = data.getLastLocation();
                    PreparedStatement stmt = connection.prepareStatement("UPDATE playerdata SET is_flying = ?, location_x = ?, location_y = ?, location_z = ?, world = ?, gamemode = ?, total_playtime = ? WHERE player_uuid = ?");
                    stmt.setBoolean(1, data.isFlying());

                    stmt.setDouble(2, location.getX());
                    stmt.setDouble(3, location.getY());
                    stmt.setDouble(4, location.getZ());

                    stmt.setString(5, location.getWorld().getName());
                    stmt.setInt(6, data.getGameMode());
                    stmt.setLong(7, data.getOnlineTime());
                    stmt.setString(8, data.getPlayer().getUniqueId().toString());

                    stmt.execute();
                    stmt.close();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    @Override
    public CompletableFuture<PlayerdataWrapper> load(String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try
            {
                Connection connection = DataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM playerdata WHERE player_uuid = ?");
                preparedStatement.setString(1, uuid);
                ResultSet result = preparedStatement.executeQuery();
                if(result.next())
                {
                    return PlayerdataWrapper.fromResultset(result);
                }
            }
            catch(SQLException exception)
            {
                System.out.println(exception.getMessage());
            }
            return null;
        });
    }

    @Override
    public void initData(PlayerdataWrapper data) {
        load(data.getUniqueId()).thenAccept(result -> new BukkitRunnable() {
            @Override
            public void run() {
                if(result != null) return;

                try {
                    Location location = data.getLastLocation();
                    PreparedStatement stmt = connection.prepareStatement("INSERT INTO playerdata (player_uuid, is_flying, location_x, location_y, location_z, world, gamemode, total_playtime) values(?, ?, ?, ?, ?, ?, ?, ?)");

                    stmt.setString(1, data.getPlayer().getUniqueId().toString());
                    stmt.setBoolean(2, data.isFlying());

                    stmt.setDouble(3, location.getX());
                    stmt.setDouble(4, location.getY());
                    stmt.setDouble(5, location.getZ());

                    stmt.setString(6, location.getWorld().getName());
                    stmt.setInt(7, data.getGameMode());
                    stmt.setLong(8, data.getOnlineTime());
                    stmt.execute();
                    stmt.close();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin));


    }
}
