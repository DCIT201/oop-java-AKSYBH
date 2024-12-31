

import java.util.*;

public class Main {
    public static void main(String[] args) {
        RentalAgency rentalCorporateorganisation = new RentalAgency();
        Scanner input = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println("\n--- Vehicle Rental Management System ---");
            System.out.println("i. Use Vehicle");
            System.out.println("ii. Use Customer");
            System.out.println("iii. Show Vehicles");
            System.out.println("iv. Reveal Customers");
            System.out.println("v. Rent  Vehicle");
            System.out.println("vi. Give Vehicle");
            System.out.println("vii. Rate Vehicle");
            System.out.println("viii. Close");
            System.out.print("Make a choice: ");

            String choice = input.nextLine();
            input.nextLine();

            switch (choice) {
                case "i" -> addVehicle(rentalCorporateorganisation, input);
                case "ii"-> addCustomer(rentalCorporateorganisation, input);
                case "iii"-> System.out.println(ObtainEverything(rentalCorporateorganisation));
                case "iv" -> System.out.println(obtainCustomers(rentalCorporateorganisation));
                case "v" -> rentVehicle(rentalCorporateorganisation, input);
                case "vi" -> returnVehicle(rentalCorporateorganisation, input);
                case "vii"-> rateVehicle(rentalCorporateorganisation, input);
                case "viii"-> {
                    running = false;
                    System.out.println("Exiting application. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        input.close();
    }

    private static void addVehicle(RentalAgency CorporateRentalAgency, Scanner input) {
        System.out.println("Which type do you want to add? \n Please select from Car, Motorcycle and Truck: ");
        String vehicleType = input.nextLine().trim().toLowerCase();

        System.out.println("\n--- Add Vehicle ---");

        System.out.print("Input a vehicle model: ");
        String model = input.nextLine();

        System.out.print("Input rental rate");
        double baseRate = input.nextDouble();

        System.out.print("Is the vehicle available now? (true/false): ");
        boolean isAvailable = input.nextBoolean();

        System.out.print("Does the vehicle have climate control? (true/false): ");
        boolean hasClimateControl = input.nextBoolean();
        input.nextLine();


        switch (vehicleType) {
            case "car":
                Vehicle car = new Car(model, baseRate, isAvailable, hasClimateControl);
                CorporateRentalAgency.addToFleet(car);
                System.out.println("Car added successfully.");
                break;
            case "motorcycle":
                System.out.println("What is the engine capacity?");
                int capacity = input.nextInt();
                Vehicle motorcycle = new Motorcycle(model, baseRate, isAvailable, capacity);
                CorporateRentalAgency.addToFleet(motorcycle);
                System.out.println("Motorcycle added successfully.");
                break;

            case "truck":
                System.out.println("What is the load quantity take?");
                int loadCapacity = input.nextInt();
                Vehicle truck = new Truck(model, baseRate, isAvailable, loadCapacity);
                CorporateRentalAgency.addToFleet(truck);
                System.out.println("Truck Included successfully.");
                break;

            default:
                System.out.println("Unacceptable vehicle type. Please try again.");
                break;
        }


    }

    private static void addCustomer(RentalAgency CorporateRentalAgency, Scanner input) {
        System.out.println("\n--- Include Customer ---");
        System.out.print("Input customer name: ");
        String name = input.nextLine();
        System.out.print("Input customer email: ");
        String email = input.nextLine();
        System.out.println("Input customer phone: ");
        String phone = input.nextLine();

        Customer customer = new Customer(name, phone, email);
        CorporateRentalAgency.addCustomer(customer);
        System.out.println("Customer Included successfully.");
    }

    private static void rentVehicle(RentalAgency CorporateRentalAgency, Scanner input) {
        System.out.println("\n--- Rent a Vehicle ---");
        System.out.println("Would you like to see a list of available vehicles? (yes/no) ");
        String answer = input.next().toLowerCase();
        input.nextLine();

        switch (answer) {
            case "yes":
                System.out.println(CorporateRentalAgency.getAvailableTypeVehicles());
                break;
            case "no":
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                return;
        }

        System.out.println("Would you like to Rent a vehicle? (yes/no) ");
        String rentAnswer = input.nextLine().toLowerCase();
        if (!rentAnswer.equals("yes")) {
            return;
        }

        System.out.print("Enter vehicle model: ");
        String model = input.nextLine();
        Optional<Vehicle> vehicle = CorporateRentalAgency.getVehicle(model);


        if (vehicle.isEmpty()) {
            System.out.println("Vehicle with model " + model + " does not exist.");
            System.out.println("Would you like to exit or return to the main menu? (enter exit to leave)");
            String choice = input.nextLine().toLowerCase();
            if (choice.equalsIgnoreCase("exit")) {
                System.out.println("Exiting application. Goodbye!");
                System.exit(0);
            }
            return;
        }
        Vehicle rentingCar = vehicle.get();


        System.out.print("Enter customer email: ");
        String email = input.nextLine();
        Optional<Customer> customer = CorporateRentalAgency.findCustomerByEmail(email);

        if (customer.isEmpty()) {
            System.out.println("Customer with email " + email + " does not exist.");
            System.out.println("Would you like to exit or return to the main menu? (enter exit to leave)");
            String choice = input.nextLine().toLowerCase();
            if (choice.equalsIgnoreCase("exit")) {
                System.out.println("Exiting application. Goodbye!");
                System.exit(0);
            }
            return;
        }
        Customer customerToRent = customer.get();

        System.out.print("Enter rental duration (days): ");
        int days = input.nextInt();
        input.nextLine();

        try {
            double cost = CorporateRentalAgency.calculateRentalCost(rentingCar, customerToRent, days);
            System.out.println("Vehicle rented successfully. Total cost: GHC" + cost);

            RentalTransaction transaction = CorporateRentalAgency.rentVehicle(rentingCar.getVehicleId(), customerToRent, days);
            System.out.println("This transaction ID you will use to return the vehicle: " + transaction.getTransactionId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    private static void handleInvalidInput(String message, Scanner input) {
        System.out.println(message);
        System.out.println("Would you like to exit or return to the main menu? (enter exit to leave)");
        String choice = input.nextLine().toLowerCase();
        if (choice.equalsIgnoreCase("exit")) {
            System.out.println("Exiting application. Goodbye!");
            System.exit(0);
        }
    }

    private static void returnVehicle(RentalAgency CorporateRentalAgency, Scanner input) {
        System.out.println("\n--- Return a Vehicle ---");
        System.out.print("Enter transaction ID: ");
        UUID transactionID = UUID.fromString(input.nextLine());


        try {
            CorporateRentalAgency.returnVehicle(transactionID);
            System.out.println("Vehicle returned successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void rateVehicle(RentalAgency CorporateRentalAgency, Scanner input) {
        System.out.println("\n--- Rate the Vehicle ---");
        System.out.print("Enter vehicle type: ");
        String model = input.nextLine();
        System.out.print("Enter the rating : ");
        int rating = input.nextInt();
        input.nextLine();
        Optional<Vehicle> vehicle = CorporateRentalAgency.getVehicle(model);
        if (vehicle.isEmpty()) {
            handleInvalidInput("Vehicle  model " + model + " null.", input);
        }
        Vehicle rentingCar = vehicle.get();

        try {
            CorporateRentalAgency.rateVehicle(rentingCar.getVehicleId(), rating);
            System.out.println("Vehicle rated successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static List<Vehicle> ObtainEverything(RentalAgency CorporateRentalAgency) {
        return CorporateRentalAgency.getFleet();
    }
    private static List<Customer> obtainCustomers(RentalAgency CorporateRentalAgency) {
        return CorporateRentalAgency.getCustomers();
    }
}
