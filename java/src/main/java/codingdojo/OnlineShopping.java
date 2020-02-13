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

  private Session session;

  public OnlineShopping(Session session) {
    this.session = session;
  }

  /**
   * This method is called when the user changes the store they are shopping at in the online
   * shopping website.
   */
  public void switchStore(Store storeToSwitchTo) {
    Cart cart = (Cart) session.get("CART");
    DeliveryInformation deliveryInformation = (DeliveryInformation) session.get("DELIVERY_INFO");
    if (cart != null) {
      if (mustSwitchToWarehouse(storeToSwitchTo)) {
        switchToWarehouse(cart, deliveryInformation);
      } else {
        switchToStore(storeToSwitchTo, cart, deliveryInformation);
      }
    }
    session.put("STORE", storeToSwitchTo);
    session.saveAll();
  }

  private void switchToStore(Store storeToSwitchTo, Cart cart,
      DeliveryInformation deliveryInformation) {
    cart.switchItemsToStore(storeToSwitchTo);
    switchDeliveryInformation(storeToSwitchTo, cart, deliveryInformation);
  }

  private void switchDeliveryInformation(Store storeToSwitchTo, Cart cart,
      DeliveryInformation deliveryInformation) {
    if (deliveryInformation == null) {
      return;
    }
    Store currentStore = (Store) session.get("STORE");

    if (isInDeliveryRange(storeToSwitchTo, deliveryInformation)
        && isHomeDeliveryAndAddress(deliveryInformation)) {
      deliveryInformation.setTotalWeight(cart.getWeight());
      deliveryInformation.setPickupLocation(storeToSwitchTo);
    }
    if (isInDeliveryRange(storeToSwitchTo, deliveryInformation)
        && !isHomeDeliveryAndAddress(deliveryInformation)) {
      deliveryInformation.setHomeDelivery();
      deliveryInformation.setTotalWeight(cart.getWeight());
      deliveryInformation.setPickupLocation(storeToSwitchTo);
    }
    if (!isInDeliveryRange(storeToSwitchTo, deliveryInformation)
        && isHomeDeliveryAndAddress(deliveryInformation)) {
      deliveryInformation.setPickup();
      deliveryInformation.setPickupLocation(currentStore);
    }
  }

  private boolean isInDeliveryRange(Store storeToSwitchTo,
      DeliveryInformation deliveryInformation) {
    return deliveryInformation
        .isInDeliveryRange(storeToSwitchTo, session.getLocationService());
  }

  private boolean isHomeDeliveryAndAddress(DeliveryInformation deliveryInformation) {
    return deliveryInformation.isHomeDelivery()
        && deliveryInformation.getDeliveryAddress() != null;
  }

  private void switchToWarehouse(Cart cart, DeliveryInformation deliveryInformation) {
    cart.switchItemsToWarehouse();
    setDeliveryTypeToShipping(deliveryInformation);
  }

  private boolean mustSwitchToWarehouse(Store storeToSwitchTo) {
    return storeToSwitchTo == null;
  }

  private void setDeliveryTypeToShipping(DeliveryInformation deliveryInformation) {
    if (deliveryInformation != null) {
      deliveryInformation.setType("SHIPPING");
      deliveryInformation.setPickupLocation(null);
    }
  }

  @Override
  public String toString() {
    return "OnlineShopping{\n"
        + "session=" + session + "\n}";
  }
}
