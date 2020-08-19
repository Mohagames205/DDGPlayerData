package me.mohamedelyousfi.playerdata;

import me.mohamedelyousfi.playerdata.listener.EventListener;
import me.mohamedelyousfi.playerdata.provider.DataProvider;
import me.mohamedelyousfi.playerdata.provider.SqlDataProvider;
import me.mohamedelyousfi.playerdata.provider.YamlDataProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Playerdata extends JavaPlugin {

    private FileConfiguration config;
    private DataProvider dataProvider;
    private static Playerdata instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        config = getConfig();

        try {
            initDataProvider();
        } catch (SQLException exception) {
            System.out.println("SOMETHING WENT WRONG");
            exception.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(new EventListener(), this);




    }

    public void initDataProvider() throws SQLException {
        if(config.getBoolean("use-mysql")){
            dataProvider = new SqlDataProvider();
        }
        else
        {
            dataProvider = new YamlDataProvider();
        }
    }

    public DataProvider getDataProvider()
    {
        return dataProvider;
    }

    public static Playerdata getInstance()
    {
        return instance;
    }




    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
