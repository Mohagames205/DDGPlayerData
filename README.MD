# DDGPlayerData


### Plugin config
````yaml
# Wanner dit true is dan wordt MySQL gebruikt als dataprovider, anders wordt YML gebruikt.
use-mysql: true

# De MySQL authenticatiegegevens
mysql:
  host: localhost:3306
  username: root
  password: wordpass321
  database: database
````

### YML Opslag methode
Voor elke speler wordt een aparte file aangemaakt met de volgende data:
```yaml
player_uuid: 6cf1c1ca-9961-32aa-bfdd-2bbf107bbf99
is_flying: false
location:
  x: 361.9750031111716
  y: 75.0
  z: 509.6675010833274
world: world
gamemode: 1
total_playtime: 459382
```

### MySQL Opslag methode
Voor elke speler wordt een aparte row aangemaakt. De UUID van de speler is de Primary Key, dit doen we zodat we de REPLACE functie in MySQL kunnen gebruiken.
````mysql
CREATE TABLE `playerdata` (
  `player_uuid` varchar(36) NOT NULL,
  `is_flying` tinyint(1) DEFAULT NULL,
  `location_x` double DEFAULT NULL,
  `location_y` double DEFAULT NULL,
  `location_z` double DEFAULT NULL,
  `world` varchar(255) DEFAULT NULL,
  `gamemode` int(11) DEFAULT NULL,
  `total_playtime` int(11) DEFAULT NULL,
  PRIMARY KEY (`player_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
````
