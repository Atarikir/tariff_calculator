package ru.fastdelivery.domain.delivery.shipment;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.fastdelivery.domain.delivery.Data.dimensions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;

class ShipmentTest {

  private Shipment shipment;

  @BeforeEach
  void setUp() {
    Weight weight1 = new Weight(BigInteger.TEN);
    Weight weight2 = new Weight(BigInteger.ONE);

    List<Pack> packages = List.of(new Pack(weight1, dimensions()), new Pack(weight2, dimensions()));
    shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"));
  }

  @AfterEach
  void tearDown() {
    shipment = null;
  }

  @Test
  void whenSummarizingWeightOfAllPackages_thenReturnSum() {
    Weight massOfShipment = shipment.weightAllPackages();

    assertThat(massOfShipment.weightGrams()).isEqualByComparingTo(BigInteger.valueOf(11));
  }

  @Test
  void whenSummarizingVolumeOfAllPackages_thenReturnSum() {
    BigDecimal volumeOfShipment = shipment.volumeAllPackages();

    assertThat(volumeOfShipment).isEqualByComparingTo(BigDecimal.valueOf(0.002));
  }
}
