package codingdojo;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Allows the OnlineShopping to access data classes and store them in the database
 */
public class Session {


  private final Map<String, ModelObject> session;

  public Session() {
    session = new HashMap<>();
    session.put("CART", new Cart());
    session.put("LOCATION_SERVICE", new LocationService());
  }

  public ModelObject get(String key) {
    return this.session.get(key);
  }

  public void put(String key, ModelObject value) {
    this.session.put(key, value);
  }

  public void saveAll() {
    session.values().stream()
        .filter(Objects::nonNull)
        .forEach(ModelObject::saveToDatabase);
  }

  @Override
  public String toString() {
    StringBuilder sessionContents = new StringBuilder("\n");
    for (Entry<String, ModelObject> entry : session.entrySet()) {
      sessionContents.append(entry.getKey());
      sessionContents.append("=");
      sessionContents.append(entry.getValue());
      sessionContents.append("\n");
    }

    return "Session{" +
        sessionContents +
        "}";
  }

  Store getStore(Session session) {
    return (Store) session.get("STORE");
  }

  Cart getCart(Session session) {
    Cart currentCart = (Cart) session.get("CART");
    if(currentCart == null) currentCart = new Cart();
    return currentCart;
  }

  DeliveryInformation getDeliveryInfo(Session session) {
    return (DeliveryInformation) session.get("DELIVERY_INFO");
  }

  void updateStore(Store storeToSwitchTo) {
    put("STORE", storeToSwitchTo);
  }
}
