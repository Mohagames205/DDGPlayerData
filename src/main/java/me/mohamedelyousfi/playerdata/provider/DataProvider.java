package me.mohamedelyousfi.playerdata.provider;

import me.mohamedelyousfi.playerdata.util.PlayerdataWrapper;

import java.util.concurrent.CompletableFuture;

public interface DataProvider {

    /**
     * Deze functie slaat/update de data op in de gekozen dataprovider
     *
     * @param data een instance van de PlayerdataWrapper zodat alle data uit deze object opgelsagen kan worden
     */
    void save(PlayerdataWrapper data);

    /**
     * Dit laadt de data async in vanuit de gekozen dataprovider.
     *
     * @param uuid
     * @return een CompleatableFuture met een PlayerdataWrapper object als de UUID bestaat, anders wordt null gereturned
     */
    CompletableFuture<PlayerdataWrapper> load(String uuid);

}
