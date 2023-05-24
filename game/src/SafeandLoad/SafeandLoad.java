package SafeandLoad;

import ecs.GeneralGenerator;
import ecs.entities.Entity;

import ecs.entities.monsters.Chort;
import ecs.entities.monsters.Goblin;
import ecs.entities.monsters.LittleChort;
import ecs.entities.nps.Ghost;
import ecs.entities.objects.Tombstone;
import starter.Game;

import java.io.*;
import java.util.ArrayList;

public class SafeandLoad {
    private final Game game;

    /**
     * Konstruktor der Klasse SafeandLoad.
     *
     * @param game das Spiel, das gespeichert oder geladen werden soll
     */
    public SafeandLoad(Game game) {
        this.game = game;
    }


    /**
     * Schreibt den aktuellen Spielstand in eine Datei.
     */
    public void writeSave() {
        DataStorage data = new DataStorage();

        // Speichert die Anzahl der Level
        data.setLevelCount(game.getLevelcounter());

        // Speichert eine Liste aller Entity-Klassen im Spiel
        ArrayList<String> entityList = new ArrayList<>();
        for (Entity entity : Game.getEntities()) {
            entityList.add(entity.getClass().getSimpleName());
        }
        data.setEntities(entityList);
        FileOutputStream fos;
        ObjectOutputStream out;
        try {
            fos = new FileOutputStream("save.ser");
            out = new ObjectOutputStream(fos);
            out.writeObject(data);
            System.out.println("gespeichert");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Lädt den Spielstand aus einer Datei.
     */
    public void loadSave() {
        FileInputStream fis;
        ObjectInputStream in;
        DataStorage data = new DataStorage();
        try {
            fis = new FileInputStream("save.ser");
            in = new ObjectInputStream(fis);
            data = (DataStorage) in.readObject();
            System.out.println(data.getEntities().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Setzt die Anzahl der Level auf den Wert aus dem gespeicherten Spielstand
        game.setLevelcounter(data.getLevelCount());

        // Erstellt Teleport-Pads für das aktuelle Level
        game.getTeleportsystem().makePads();

        // Fügt alle Entities aus dem gespeicherten Spielstand zum Spiel hinzu
        for (String entity : data.getEntities()) {
            switch (entity) {
                case "Chort" -> Game.getEntitiesToAdd().add(new Chort(GeneralGenerator.getInstance().getStrongMonsterItems(2)));
                case "Goblin" -> Game.getEntitiesToAdd().add(new Goblin(GeneralGenerator.getInstance().getWeakMonsterItems(2)));
                case "LittleChort" -> Game.getEntitiesToAdd().add(new LittleChort(GeneralGenerator.getInstance().getWeakMonsterItems(1)));
            }
            System.out.println("geladen");
        }
    }
}
