package me.mohamedelyousfi.playerdata;

import me.mohamedelyousfi.playerdata.listener.EventListener;
import me.mohamedelyousfi.playerdata.provider.DataProvider;
import me.mohamedelyousfi.playerdata.provider.SqlDataProvider;
import me.mohamedelyousfi.playerdata.provider.YamlDataProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Playerdata extends JavaPlugin {

    private DataProvider dataProvider;
    private static Playerdata instance;

    @Override
    public void onEnable() {

        instance = this;
        saveDefaultConfig();

        // Data provider instellen op basis van de config
        try {
            initDataProvider();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Event listener registreren bij de eventhandler
        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    /**
     * Deze functie stelt de DataProvider in op basis van de config.
     * Als use-mysql = true dan wordt de SqlDataProvider gebruikt
     * anders wordt de YamlDataProvider gebruikt.
     *
     * @throws Exception
     */
    public void initDataProvider() throws Exception {
        if (getConfig().getBoolean("use-mysql")) {
            dataProvider = new SqlDataProvider();
        } else {
            dataProvider = new YamlDataProvider();
        }
    }

    /**
     * @return De geregistreerde dataprovider
     */
    public DataProvider getDataProvider() {
        return dataProvider;
    }

    /**
     * @return instance van de Plugin class
     */
    public static Playerdata getInstance() {
        return instance;
    }
}
