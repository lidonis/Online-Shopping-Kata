package codingdojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * While shopping online in a Store, the Cart stores the Items you intend to buy
 */
public class Cart implements ModelObject {
    ArrayList<Item> items = new ArrayList<>();
    ArrayList<Item> unavailableItems = new ArrayList<>();
    public List<Item> getItems() {
        return items;
    }
    public void addItem(Item item) {
        this.items.add(item);
    }
    public void addItems(Collection<Item> items) {
        this.items.addAll(items);
    }

    public void markAsUnavailable(Item item) {
        this.unavailableItems.add(item);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "items=" + displayItems(items) +
                "unavailable=" + displayItems(unavailableItems) +
                '}';
    }

    private String displayItems(List<Item> items) {
        StringBuilder itemDisplay = new StringBuilder("\n");
        for (Item item : items) {
            itemDisplay.append(item.toString());
            itemDisplay.append("\n");
        }
        return itemDisplay.toString();
    }

    @Override
    public void saveToDatabase() {
        throw new UnsupportedOperationException("missing from this exercise - shouldn't be called from a unit test");
    }

    public Collection<Item> getUnavailableItems() {
        return unavailableItems;
    }

    long getWeight() {
        return getItems().stream().filter(this::isItemAvailable).mapToLong(Item::getWeight).sum();
    }

    private boolean isItemAvailable(Item item) {
        return !getUnavailableItems().contains(item);
    }

    void modifyCart(Store storeToSwitchTo) {
        if (storeToSwitchTo == null) {
            for (Item item : getItems()) {
                if ("EVENT".equals(item.getType())) {
                    markAsUnavailable(item);
                }
            }
        } else {
            ArrayList<Item> newItems = new ArrayList<>();
            for (Item item : getItems()) {
                if ("EVENT".equals(item.getType())) {
                    markAsUnavailable(item);
                    if (storeToSwitchTo.hasItem(item)) {
                        newItems.add(storeToSwitchTo.getItem(item.getName()));
                    }
                } else if (!storeToSwitchTo.hasItem(item)) {
                    markAsUnavailable(item);
                }
            }
            addItems(newItems);
        }
    }
}
