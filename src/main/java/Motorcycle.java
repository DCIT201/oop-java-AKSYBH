

import java.util.Objects;
import java.util.UUID;

public non-sealed class Motorcycle extends Vehicle implements Rentable {

    private int engineCapacity;

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public Motorcycle(String model, double baseRentalRate, boolean isAvailable, int engineCapacity) {
        super(model, baseRentalRate, isAvailable);
        this.engineCapacity = engineCapacity;
    }

    @Override
    public double calculateRentalCost(int daysRented) {
        return getBaseRentalRate() + engineCapacity * 0.2 * daysRented;
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }

    @Override
    public String toString() {
        return "Car{" +
                "vehicleId='" + getVehicleId() + '\'' +
                ", model='" + getModel() + '\'' +
                ", baseRentalRate=" + getBaseRentalRate() +
                ", isAvailable=" + isAvailable() +
                ", engineCapacity=" + engineCapacity +
                '}';
    }

    @Override
    public double Rent(Customer customer, int days) {
        if(!isAvailableForRental()) {
            throw new VehicleNotAvailable("This motorcycle is not available for Rent");
        }
        setAvailable(false);
        return calculateRentalCost(days);
    }

    @Override
    public void returnTheVehicle() {
        setAvailable(true);
    }

    @Override
    public boolean isObtainabletorent() {
        return isAvailableForRental();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Motorcycle that = (Motorcycle) o;
        return engineCapacity == that.engineCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), engineCapacity);
    }
}
