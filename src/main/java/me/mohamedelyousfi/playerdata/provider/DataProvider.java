package me.mohamedelyousfi.playerdata.provider;

import me.mohamedelyousfi.playerdata.Playerdata;
import me.mohamedelyousfi.playerdata.util.PlayerdataWrapper;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public interface DataProvider {

    void save(PlayerdataWrapper data);

    CompletableFuture<PlayerdataWrapper> load(String uuid) throws SQLException;

    void initData(PlayerdataWrapper data);

}
