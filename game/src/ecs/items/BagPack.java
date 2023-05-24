package ecs.items;

import logging.CustomLogLevel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BagPack extends ItemData {

    private final Logger backpackLogger = Logger.getLogger(this.getClass().getName());
    List<ItemData> inv = new ArrayList<>();
    int leanght = 10;



    /**
     * adds a Item to the bag if it isnt full and the item type fits
     *
     * @param itemData
     * @return true if succesfully added to bag
     */
    public boolean addItem(ItemData itemData) {
        if (inv.size() != leanght) {
            if (!inv.isEmpty()) {
                if (inv.get(0).getItemType() == itemData.getItemType()) {
                    inv.add(itemData);
                    backpackLogger.log(CustomLogLevel.INFO, (itemData.getClass().getName() + "  has been added to " + this.getClass().getName()));
                    return true;
                } else {
                    backpackLogger.log(CustomLogLevel.INFO, (itemData.getClass().getName() + "  has not been added to " + this.getClass().getName() + "because of different Types"));
                    return false;
                }
            } else {
                inv.add(itemData);
                backpackLogger.log(CustomLogLevel.INFO, (itemData.getClass().getName() + "  has been added to " + this.getClass().getName()));
                return true;
            }
        } else {
            backpackLogger.log(CustomLogLevel.INFO, (itemData.getClass().getName() + "  has not been added to " + this.getClass().getName() + "because Bagpack is full"));
            return false;
        }
    }

    public void removeItem(ItemData itemData) {
        inv.remove(itemData);
        backpackLogger.log(CustomLogLevel.INFO, (itemData.getClass().getName() + "  has been removed from " + this.getClass().getName()));
    }

    public List<ItemData> getItems() {
        return inv;
    }
}
