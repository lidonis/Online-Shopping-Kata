package codingdojo;

import java.util.ArrayList;

/**
 * The online shopping company owns a chain of Stores selling
 * makeup and beauty products.
 * <p>
 * Customers using the online shopping website can choose a Store then
 * can put Items available at that store into their Cart.
 * <p>
 * If no store is selected, then items are shipped from
 * a central warehouse.
 */
public class OnlineShopping {

    private Session session;
    private Cart cart;
    private DeliveryInformation deliveryInformation;
    private LocationService locationService;
    private Store currentStore;

    public OnlineShopping(Session session) {
        this.session = session;
        cart = (Cart) session.get("CART");
        deliveryInformation = (DeliveryInformation) session.get("DELIVERY_INFO");
        locationService = ((LocationService) session.get("LOCATION_SERVICE"));
        currentStore = (Store) session.get("STORE");
    }

    /**
     * This method is called when the user changes the
     * store they are shopping at in the online shopping
     * website.
     */
    public void switchStore(Store storeToSwitchTo) {
        if (cart != null) {
            modifyCart(storeToSwitchTo);
            modifyDeliveryInfo(storeToSwitchTo);
        }
        session.put("STORE", storeToSwitchTo);
        session.saveAll();
    }

    private void modifyDeliveryInfo(Store storeToSwitchTo) {
        if (storeToSwitchTo == null) {
            centralWarehouse();
        } else {
            store(storeToSwitchTo);
        }
    }

    private void store(Store storeToSwitchTo) {
        if (isDeliveryInfoSpecified() && deliveryInformation.isDeliveryAddressSpecified()) {
            if (isWithinDeliveryRange(storeToSwitchTo)) {
                deliveryInformation.setType("HOME_DELIVERY");
                deliveryInformation.setTotalWeight(cart.getWeight());
                deliveryInformation.setPickupLocation(storeToSwitchTo);
            } else if ("HOME_DELIVERY".equals(deliveryInformation.getType())) {
                deliveryInformation.setType("PICKUP");
                deliveryInformation.setPickupLocation(currentStore);
            }
        }
    }

    private void centralWarehouse() {
        if (isDeliveryInfoSpecified()) {
            deliveryInformation.setType("SHIPPING");
            deliveryInformation.setPickupLocation(null);
        }
    }

    private void modifyCart(Store storeToSwitchTo) {
        if (storeToSwitchTo == null) {
            changeCartWarehouse();
        } else {
            changeCartStore(storeToSwitchTo);
        }
    }

    private boolean isDeliveryInfoSpecified() {
        return deliveryInformation != null;
    }

    private void changeCartWarehouse() {
        for (Item item : cart.getItems()) {
            if ("EVENT".equals(item.getType())) {
                cart.markAsUnavailable(item);
            }
        }
    }

    private void changeCartStore(Store storeToSwitchTo) {
        ArrayList<Item> newItems = new ArrayList<>();
        for (Item item : cart.getItems()) {
            if ("EVENT".equals(item.getType())) {
                cart.markAsUnavailable(item);
                if (storeToSwitchTo.hasItem(item)) {
                    newItems.add(storeToSwitchTo.getItem(item.getName()));
                }
            } else if (!storeToSwitchTo.hasItem(item)) {
                cart.markAsUnavailable(item);
            }
        }
        cart.addItems(newItems);
    }

    private boolean isWithinDeliveryRange(Store storeToSwitchTo) {
        return locationService.isWithinDeliveryRange(storeToSwitchTo, deliveryInformation.getDeliveryAddress());
    }

    @Override
    public String toString() {
        return "OnlineShopping{\n"
                + "session=" + session + "\n}";
    }
}
