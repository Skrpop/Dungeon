package ecs;

import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.components.VelocityComponent;
import ecs.entities.Entity;
import ecs.items.*;
import graphic.Animation;
import logging.CustomLogLevel;
import tools.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**Used to Generate everything that is a Item
 * It uses the already existing Gernerators as help
 * ItemDataGenerator = itemGen
 * WorldIteBuilder = worldGen
 */

public class GeneralGenerator {
    private static GeneralGenerator inst= null;
    ItemDataGenerator itemGen = new ItemDataGenerator();
    WorldItemBuilder worldGen = new WorldItemBuilder();
    private  final Logger generalGenLogger = Logger.getLogger(this.getClass().getName());
    public static final List<String> DEFAULT_BAGPACK = List.of("Items/Backs/BagPack.png");

    /**
     * General generator is a Singelton with lazy loading
     */

    private GeneralGenerator(){
        generalGenLogger.log(CustomLogLevel.INFO,"General Generator was created");
    }

    public static GeneralGenerator getInstance(){
        if (inst == null) {
            inst = new GeneralGenerator();
        }
        return inst;
    }

    /**
     * creates the Itemdata for the Magic book with the itemGen
     * then makes it into a Worlditem with the worlGen#
     * (this spawnes the Item in a random space on the map)
     */
    public void createMagicBook(){
        ItemData item = itemGen.generateItemData(3);
        item.setOnUse(this::useBook);
        worldGen.buildWorldItem(item,true);
        generalGenLogger.log(CustomLogLevel.INFO,item.getItemName()+"of type: "+item.getItemType()+" was created and has been added to the Game");
    }

    /**
     * creates a droped item
     * @param itemData   ItemData of the dropped Item
     * @param pos        Position the Item will be dropped(gotten by the entity that drops the Item)
     */
    public void dropItems(ItemData itemData, Point pos){
        worldGen.buildWorldItem(itemData,pos);
        generalGenLogger.log(CustomLogLevel.INFO,"Item: "+itemData.getItemName()+" was dropped at"+pos.x,pos.y);
    }

    /**
     * creates the Loot that weaker moster (Goblin and littleChort) will drop
     * @param amount  the amount of items these Monsters will drop.
     * @return
     */

    public List<ItemData> getWeakMonsterItems(int amount){
        List<ItemData> temp = new ArrayList<>();
        for(int i=0;i<amount;i++){
            ItemData item = itemGen.generateItemData(4);//Food
            item.setOnUse(this::useFood);
            temp.add(item);
            generalGenLogger.log(CustomLogLevel.INFO,item.getItemName()+"of type: "+item.getItemType()+" was created and will be added to inventory");
        }
        return temp;
    }

    /**
     * creates the loot for stronger monster
     * this Monster drops two different types of Loot Food and Potion
     * Potions are worth more, so they take 2 Item amounts
     * @param amount
     * @return
     */
    public List<ItemData> getStrongMonsterItems(int amount){
        List<ItemData> temp = new ArrayList<>();
        for(int i=0;i<amount;i++){
            if(i%2==0){
                ItemData item = itemGen.generateItemData(5);//Potion
                item.setOnUse(this::usePotion);
                temp.add(item);
                i++;        // worth more
                generalGenLogger.log(CustomLogLevel.INFO,item.getItemName()+"of type: "+item.getItemType()+" was created and will be added to inventory");
            }else{
                ItemData item = itemGen.generateItemData(4);//Food
                item.setOnUse(this::useFood);
                temp.add(item);
                generalGenLogger.log(CustomLogLevel.INFO,item.getItemName()+"of type: "+item.getItemType()+" was created and will be added to inventory");
            }
        }
        return temp;
    }

    /**
     * since the bagpack is a Special Item it isnt created by the ItemDataGenerator
     * however in order to drop it the worldGen will still be used
     */
    public void makeBagPack(){
        ItemData bag = new ItemData(ItemType.Bag,
            new Animation(DEFAULT_BAGPACK, 1),
            new Animation(DEFAULT_BAGPACK, 1),
            "Rucksack",
            "Kann Items Enthalten");
        worldGen.buildWorldItem(bag,true);
        generalGenLogger.log(CustomLogLevel.INFO,bag.getItemName()+"of type: "+bag.getItemType()+" was created and will be added map");
    }

    /**
     * iOnUse method that food Items will be given since they don´t just vanish but also replenish HP
     * @param e
     * @param item
     */
    public void useFood(Entity e,ItemData item){
        e.getComponent(InventoryComponent.class)
            .ifPresent(
                component -> {
                    InventoryComponent invComp = (InventoryComponent) component;
                    invComp.removeItem(item);
                });
        e.getComponent(HealthComponent.class)
            .ifPresent(
                component -> {
                    HealthComponent healthcomp = (HealthComponent) component;
                    healthcomp.setCurrentHealthpoints(healthcomp.getCurrentHealthpoints()+2);
                    System.out.println("Current health: "+healthcomp.getMaximalHealthpoints()+" / "+healthcomp.getCurrentHealthpoints());
                }
            );
    }
    /**
     * iOnUse method that BookItems will be given since they don´t just vanish but also increase HP
     * @param e
     * @param item
     */
    public void useBook(Entity e,ItemData item){
        e.getComponent(InventoryComponent.class)
            .ifPresent(
                component -> {
                    InventoryComponent invComp = (InventoryComponent) component;
                    invComp.removeItem(item);
                });
        e.getComponent(HealthComponent.class)
            .ifPresent(
                component -> {
                    HealthComponent healthcomp = (HealthComponent) component;
                    healthcomp.setMaximalHealthpoints(healthcomp.getMaximalHealthpoints()+5);
                    System.out.println("Current health: "+healthcomp.getMaximalHealthpoints()+" / "+healthcomp.getCurrentHealthpoints());
                }
            );
    }
    /**
     * iOnUse method that Potion Items will be given since they don´t just vanish but also increase Speed
     * @param e
     * @param item
     */
    public void usePotion(Entity e,ItemData item){
        e.getComponent(InventoryComponent.class)
            .ifPresent(
                component -> {
                    InventoryComponent invComp = (InventoryComponent) component;
                    invComp.removeItem(item);
                });
        e.getComponent(VelocityComponent.class)
            .ifPresent(
                component -> {
                    VelocityComponent velcomp = (VelocityComponent) component ;
                    velcomp.setXVelocity((float) (velcomp.getXVelocity()+0.1));
                    velcomp.setYVelocity((float) (velcomp.getYVelocity()+0.1));
                }
            );
    }
}
