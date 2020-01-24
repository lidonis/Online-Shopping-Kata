package codingdojo;

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
            cart.modifyCart(storeToSwitchTo);
            if(deliveryInformation != null) {
                deliveryInformation.modifyDeliveryInfo(storeToSwitchTo, locationService, currentStore, cart.getWeight());
            }
        }
        session.put("STORE", storeToSwitchTo);
        session.saveAll();
    }

    @Override
    public String toString() {
        return "OnlineShopping{\n"
                + "session=" + session + "\n}";
    }
}
