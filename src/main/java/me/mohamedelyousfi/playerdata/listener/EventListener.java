package me.mohamedelyousfi.playerdata.listener;

import me.mohamedelyousfi.playerdata.Playerdata;
import me.mohamedelyousfi.playerdata.provider.DataProvider;
import me.mohamedelyousfi.playerdata.util.PlayerdataWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

/**
 * Wanneer een van de onderstaande acties wordt getriggered wordt alle data in de database/ymlfile geupdate
 */
public class EventListener implements Listener {

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {
        commitData(event);
    }

    @EventHandler
    public void onFlyToggle(PlayerToggleFlightEvent event) {
        commitData(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        commitData(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        commitData(event);
    }

    public void commitData(PlayerEvent event)
    {
        Player player = event.getPlayer();
        PlayerdataWrapper playerdataWrapper = new PlayerdataWrapper(player, player.getUniqueId().toString(), player.isFlying(), player.getLocation(), player.getGameMode().getValue(), player.getPlayerTime());
        DataProvider dataProvider = Playerdata.getInstance().getDataProvider();
        dataProvider.save(playerdataWrapper);
    }


}
