package me.mohamedelyousfi.playerdata.provider;

import me.mohamedelyousfi.playerdata.Playerdata;
import me.mohamedelyousfi.playerdata.util.PlayerdataWrapper;

import java.util.concurrent.CompletableFuture;

public class YamlDataProvider implements DataProvider {

    private final Playerdata plugin;

    public YamlDataProvider() {
        plugin = Playerdata.getInstance();
    }

    @Override
    public void save(PlayerdataWrapper data) {

    }

    @Override
    public CompletableFuture<PlayerdataWrapper> load(String uuid) {
        return null;
    }

    @Override
    public void initData(PlayerdataWrapper data) {
        load(data.getUniqueId()).thenAccept(result -> {

        });
    }
}
