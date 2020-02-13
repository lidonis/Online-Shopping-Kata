package codingdojo;

/**
 * The online shopping company owns a chain of Stores selling makeup and beauty products.
 * <p>
 * Customers using the online shopping website can choose a Store then can put Items available at
 * that store into their Cart.
 * <p>
 * If no store is selected, then items are shipped from a central warehouse.
 */
public class OnlineShopping {

  private final Session session;
  private final DeliveryService deliveryService;

  public OnlineShopping(Session session) {
    this.session = session;
    deliveryService = new DeliveryService(((LocationService) session.get("LOCATION_SERVICE")));
  }

  /**
   * This method is called when the user changes the store they are shopping at in the online
   * shopping website.
   */
  public void switchStore(Store storeToSwitchTo) {
    Cart cart = session.getCart(session);
    cart.updateCart(storeToSwitchTo);
    updateDeliveryInfo(storeToSwitchTo, cart.getWeight());
    session.updateStore(storeToSwitchTo);
    session.saveAll();
  }

  private void updateDeliveryInfo(Store storeToSwitchTo, long cartWeight) {
    DeliveryInformation deliveryInfo = session.getDeliveryInfo(session);
      deliveryService.updateDeliveryInfo(deliveryInfo, storeToSwitchTo,
          session.getStore(session),
          cartWeight);
  }

  @Override
  public String toString() {
    return "OnlineShopping{\n"
        + "session=" + session + "\n}";
  }
}
