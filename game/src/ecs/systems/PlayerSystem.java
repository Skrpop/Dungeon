package ecs.systems;

import com.badlogic.gdx.Gdx;
import configuration.KeyboardConfig;
import ecs.components.InventoryComponent;
import ecs.components.MissingComponentException;
import ecs.components.PlayableComponent;
import ecs.components.VelocityComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import ecs.items.BagPack;
import ecs.items.ItemData;
import ecs.items.ItemType;
import ecs.tools.interaction.InteractionTool;
import starter.Game;

import java.util.List;
import java.util.stream.Stream;

/** Used to control the player */
public class PlayerSystem extends ECS_System {

    private record KSData(Entity e, PlayableComponent pc, VelocityComponent vc) {}

    @Override
    public void update() {
        Game.getEntities().stream()
                .flatMap(e -> e.getComponent(PlayableComponent.class).stream())
                .map(pc -> buildDataObject((PlayableComponent) pc))
                .forEach(this::checkKeystroke);
    }

    private void checkKeystroke(KSData ksd) {
        if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_UP.get()))
            ksd.vc.setCurrentYVelocity(1 * ksd.vc.getYVelocity());
        else if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_DOWN.get()))
            ksd.vc.setCurrentYVelocity(-1 * ksd.vc.getYVelocity());
        else if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_RIGHT.get()))
            ksd.vc.setCurrentXVelocity(1 * ksd.vc.getXVelocity());
        else if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_LEFT.get()))
            ksd.vc.setCurrentXVelocity(-1 * ksd.vc.getXVelocity());

        if (Gdx.input.isKeyPressed(KeyboardConfig.INTERACT_WORLD.get()))
            InteractionTool.interactWithClosestInteractable(ksd.e);
        if (Gdx.input.isKeyPressed(KeyboardConfig.INVENTORY_OPEN.get()))
            showInv();

        // check skills
        else if (Gdx.input.isKeyPressed(KeyboardConfig.FIRST_SKILL.get()))
            ksd.pc.getSkillSlot1().ifPresent(skill -> skill.execute(ksd.e));
        else if (Gdx.input.isKeyPressed(KeyboardConfig.SECOND_SKILL.get()))
            ksd.pc.getSkillSlot2().ifPresent(skill -> skill.execute(ksd.e));
        //use Items
        else if (Gdx.input.isKeyPressed(KeyboardConfig.USE_MAGICBOOK.get())){
            useMagicBook();
        }else if (Gdx.input.isKeyPressed(KeyboardConfig.USE_FLEISCH.get())){
            useFood();
        }else if (Gdx.input.isKeyPressed(KeyboardConfig.USE_POTION.get())){
            usePotion();
        }else if (Gdx.input.isKeyPressed(KeyboardConfig.ADD_TO_BACk.get())){
            addToBack();
        }
    }

    private KSData buildDataObject(PlayableComponent pc) {
        Entity e = pc.getEntity();

        VelocityComponent vc =
                (VelocityComponent)
                        e.getComponent(VelocityComponent.class)
                                .orElseThrow(PlayerSystem::missingVC);

        return new KSData(e, pc, vc);
    }

    /**
     * shows The inventory in the console
     */

    public void showInv(){
        InventoryComponent invCom = (InventoryComponent)Game.getHero().get().getComponent(InventoryComponent.class).get();
        List<ItemData> inv = invCom.getItems();
        System.out.println("Inventar:");
        int counter=1;
        for(ItemData i:inv){
            System.out.println(counter+": "+i.getItemName()+"    ("+i.getItemType()+")");
            counter++;
        }
    }

    /**
     * uses Available Magicbook in the Inventory
     */
    public void useMagicBook(){
        Hero hero = (Hero) Game.getHero().get();
        InventoryComponent inv = (InventoryComponent) hero.getComponent(InventoryComponent.class).get();
        List<ItemData> items = inv.getItems();
        boolean done=false;
        for(ItemData i:items){  //search for Book item
            if(i.getItemType()==ItemType.Book&&!done){    //item found
                i.triggerUse(hero);                       //use that item
                done=true;
            }
        }
    }

    /**
     * uses available Potion in the Inventory
     */
    public void usePotion(){
        Hero hero = (Hero) Game.getHero().get();
        InventoryComponent inv = (InventoryComponent) hero.getComponent(InventoryComponent.class).get();
        List<ItemData> items = inv.getItems();
        boolean done=false;
        for(ItemData i:items){        //search for Potion item
            if(i.getItemType()==ItemType.Potion&&!done){   //item found
                i.triggerUse(hero);                //use
                done=true;
            }
        }
    }

    /**
     * uses available Food in the Inventory
     */
    public void useFood(){
        Hero hero = (Hero) Game.getHero().get();
        InventoryComponent inv = (InventoryComponent) hero.getComponent(InventoryComponent.class).get();
        List<ItemData> items = inv.getItems();
        boolean done=false;
        for(ItemData i:items){         //search for food item
            if(i.getItemType()==ItemType.Food&&!done){    // first food item found
                i.triggerUse(hero);                       //use it
                done=true;
            }
        }
    }

    private static MissingComponentException missingVC() {
        return new MissingComponentException("VelocityComponent");
    }

    /**
     * searches the inventory for bag packs and tries to add a item to it
     */
    public void addToBack() {
        Hero hero = (Hero) Game.getHero().get();
        InventoryComponent inv = (InventoryComponent) hero.getComponent(InventoryComponent.class).get();
        List<ItemData> items = inv.getItems();
        boolean done = false;
        for (ItemData b : items) {    // searching for bag
            if (b.getItemType() == ItemType.Bag && !done) {  // searching for bag
                for (ItemData i : items) {           // searching for item
                    if (i.getItemType() != ItemType.Bag && !done) {    // searching for item
                        done = ((BagPack) b).addItem(i);
                        inv.removeItem(i);
                    }
                }
            }
        }
    }
}
