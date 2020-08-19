package me.mohamedelyousfi.playerdata.listener;

import me.mohamedelyousfi.playerdata.Playerdata;
import me.mohamedelyousfi.playerdata.provider.DataProvider;
import me.mohamedelyousfi.playerdata.util.PlayerdataWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class EventListener implements Listener {

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event)
    {
        Player player = event.getPlayer();
        PlayerdataWrapper playerdataWrapper = new PlayerdataWrapper(player, player.getUniqueId().toString(), player.isFlying(), player.getLocation(), event.getNewGameMode().getValue(), player.getPlayerTime());
        DataProvider dataProvider = Playerdata.getInstance().getDataProvider();
        dataProvider.save(playerdataWrapper);
    }

    @EventHandler
    public void onFlyToggle(PlayerToggleFlightEvent event)
    {
        Player player = event.getPlayer();
        PlayerdataWrapper playerdataWrapper = new PlayerdataWrapper(player, player.getUniqueId().toString(), event.isFlying(), player.getLocation(), player.getGameMode().getValue(), player.getPlayerTime());
        DataProvider dataProvider = Playerdata.getInstance().getDataProvider();
        dataProvider.save(playerdataWrapper);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        PlayerdataWrapper playerdataWrapper = new PlayerdataWrapper(player, player.getUniqueId().toString(), player.isFlying(), player.getLocation(), player.getGameMode().getValue(), player.getPlayerTime());
        DataProvider dataProvider = Playerdata.getInstance().getDataProvider();
        dataProvider.save(playerdataWrapper);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        PlayerdataWrapper playerdataWrapper = new PlayerdataWrapper(player, player.getUniqueId().toString(), player.isFlying(), player.getLocation(), player.getGameMode().getValue(), player.getPlayerTime());

        DataProvider dataProvider = Playerdata.getInstance().getDataProvider();
        dataProvider.initData(playerdataWrapper);
    }


}
