package codingdojo;

public class DeliveryService {

  private final LocationService locationService;

  public DeliveryService(LocationService locationService) {
    this.locationService = locationService;
  }

  void updateDeliveryInfo(DeliveryInformation deliveryInformation, Store storeToSwitchTo,
      Store currentStore, long cartWeight) {
    if (isCentralShipping(storeToSwitchTo)) {
      deliveryInformation.switchToShipping();
    } else {
      if (deliveryInformation.isDeliveryAddressSpecified()) {
        if (canDoHomeDelivery(deliveryInformation, storeToSwitchTo)) {
          deliveryInformation.switchToHomeDelivery(storeToSwitchTo, cartWeight);
        } else if (deliveryInformation.isHomeDelivery()) {
          deliveryInformation.switchToPickUp(currentStore);
        }
      }
    }
  }

  private boolean isCentralShipping(Store storeToSwitchTo) {
    return storeToSwitchTo == null;
  }

  private boolean canDoHomeDelivery(DeliveryInformation deliveryInfo, Store storeToSwitchTo) {
    return locationService
        .isWithinDeliveryRange(storeToSwitchTo, deliveryInfo.getDeliveryAddress());
  }
}
