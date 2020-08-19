package me.mohamedelyousfi.playerdata.provider;

import me.mohamedelyousfi.playerdata.Playerdata;
import me.mohamedelyousfi.playerdata.provider.mysql.DataSource;
import me.mohamedelyousfi.playerdata.util.PlayerdataWrapper;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class SqlDataProvider implements DataProvider {


    private final Playerdata plugin;

    /**
     * De constructor maakt de table aan in de database als deze nog niet bestaat.
     *
     * @throws SQLException
     */
    public SqlDataProvider() throws SQLException {
        Connection connection = DataSource.getConnection();
        plugin = Playerdata.getInstance();
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS playerdata(player_uuid VARCHAR(36), is_flying BOOLEAN, location_x DOUBLE, location_y DOUBLE, location_z DOUBLE, world VARCHAR(255), gamemode int, total_playtime int, primary key (player_uuid))");
    }

    /**
     * @param data
     * @see DataProvider#save(PlayerdataWrapper) voor documentatie
     */
    @Override
    public void save(PlayerdataWrapper data) {
        // De SQL Queries moeten async uitgevoerd worden zodat ze de Main Thread niet blokkeren
        new BukkitRunnable() {
            @Override
            public void run() {
                try {

                    // De data in de database inserten/updaten
                    Location location = data.getLastLocation();
                    Connection connection = DataSource.getConnection();
                    PreparedStatement stmt = connection.prepareStatement("REPLACE INTO playerdata (player_uuid, is_flying, location_x, location_y, location_z, world, gamemode, total_playtime) values(?, ?, ?, ?, ?, ?, ?, ?)");

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
        }.runTaskAsynchronously(plugin);
    }

    /**
     * @param uuid
     * @return een CompletableFuture met de PlayerdataWrapper
     * @see DataProvider#load(String)
     */
    @Override
    public CompletableFuture<PlayerdataWrapper> load(String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // De data van de speler ophalen uit de database
                Connection connection = DataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM playerdata WHERE player_uuid = ?");
                preparedStatement.setString(1, uuid);

                ResultSet result = preparedStatement.executeQuery();
                preparedStatement.close();
                // Alleen PlayerdataWrapper returnen als er wel degeleijk een resultaat beschikbaar is.
                if (result.next()) {
                    return PlayerdataWrapper.fromResultset(result);
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return null;
        });
    }
}
