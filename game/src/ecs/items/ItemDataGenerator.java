package ecs.items;

import graphic.Animation;
import java.util.List;
import java.util.Random;

/** Generator which creates a random ItemData based on the Templates prepared. */
public class ItemDataGenerator {
    private static final List<String> missingTexture = List.of("animation/missingTexture.png");
    private static final List<String> bookTexture = List.of("Items/Book/MagicBook.png");
    private static final List<String> potionTexture = List.of("Items/Potion/Zaubertzrank.png");
    private static final List<String> steakTexture = List.of("Items/Food/Steak.png");
    public static final List<String> DEFAULT_BAGPACK = List.of("Items/Backs/BagPack.png");

    private List<ItemData> templates =
            List.of(
                    new ItemData(
                            ItemType.Basic,
                            new Animation(missingTexture, 1),
                            new Animation(missingTexture, 1),
                            "Buch",
                            "Ein sehr lehrreiches Buch."),
                    new ItemData(
                            ItemType.Basic,
                            new Animation(missingTexture, 1),
                            new Animation(missingTexture, 1),
                            "Tuch",
                            "Ein sauberes Tuch.."),
                    new ItemData(
                            ItemType.Basic,
                            new Animation(missingTexture, 1),
                            new Animation(missingTexture, 1),
                            "Namensschild",
                            "Ein Namensschild wo der Name nicht mehr lesbar ist.."),
                    new ItemData(                                    //the new Item data templates
                            ItemType.Book,
                            new Animation(bookTexture, 1),
                            new Animation(bookTexture, 1),
                            "Magicbook",
                            "Ein Buch das die Maximalen Hp des Helden erhöht"),
                    new ItemData(
                            ItemType.Food,
                            new Animation(steakTexture, 1),
                            new Animation(steakTexture, 1),
                            "Fleisch",
                            "Regeneriert die Leben des Helden"),
                    new ItemData(
                            ItemType.Potion,
                            new Animation(potionTexture, 1),
                            new Animation(potionTexture, 1),
                            "Zaubertrank der Geschwindigkeit",
                            "Erhöht die Geschwindigkeit des Helden"),
                    new ItemData(
                            ItemType.Bag,
                            new Animation(DEFAULT_BAGPACK, 1),
                            new Animation(DEFAULT_BAGPACK, 1),
                            "Rucksack",
                            "Kann Items Enthalten"
                            ));

    private Random rand = new Random();

    /**
     * @return a new randomItemData
     */
    public ItemData generateItemData() {
        return templates.get(rand.nextInt(templates.size()));
    }

    /**
     * returnes a specific Item not a random one
     * @param ItemIndex
     * @return
     */
    public ItemData generateItemData(int ItemIndex) {
        return templates.get(ItemIndex);
    }
}
