// Name: Saadia Ali
//Student ID: 501227915
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
//import 
/*
 * 
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all users, drivers and service requests (RIDE or DELIVERY)
 * 
 */
public class TMUberSystemManager
{
  private ArrayList<User>   users;
  private ArrayList<Driver> drivers;

  private ArrayList<TMUberService> serviceRequests; 

  public double totalRevenue; // Total revenues accumulated via rides and deliveries
  
  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  //These variables are used to generate user account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  public TMUberSystemManager()
  {
    users   = new ArrayList<User>();
    drivers = new ArrayList<Driver>();
    serviceRequests = new ArrayList<TMUberService>(); 
    
    TMUberRegistered.loadPreregisteredUsers(users);
    TMUberRegistered.loadPreregisteredDrivers(drivers);
    
    totalRevenue = 0;
  }

  // General string variable used to store an error message when something is invalid 
  // (e.g. user does not exist, invalid address etc.)  
  // The methods below will set this errMsg string and then return false
  String errMsg = null;

  public String getErrorMessage()
  {
    return errMsg;
  }
  
  // Given user account id, find user in list of users
  // Return null if not found
  public User getUser(String accountId)
  {
    // Fill in the code 
    for (User i: users) { 
      if (i.getAccountId().equals(accountId)) { // checks if any user from the array list users, is equal to user
        return i; // finds the user and returns it
      }
    }
    return null; // return null if user not found
  }
  
  // Check for duplicate user
  private boolean userExists(User user)
  {
    // Fill in the code
    for (User i: users) { 
      if (i.equals(user)) { // checks to see if any user from the list is equal to user (duplicate)
        return true;
      }
    }
    return false;
  }
  
 // Check for duplicate driver
 private boolean driverExists(Driver driver)
 {
   // Fill in the code
   for (Driver i: drivers) { 
      if (i.equals(driver)) { // checks if any driver from arraylists drivers equal to driver to find any duplicates
        return true;
      }
   }
   return false;
 }
  
  // Given a user, check if user ride/delivery request already exists in service requests
  private boolean existingRequest(TMUberService req)
  {
    // Fill in the code
    for (TMUberService i: serviceRequests) {
      if (i.equals(req)) { // checks if any service request is equal to req, to see if req already exists
        return true;
      }
    }
    return false;
  }

  // Calculate the cost of a ride or of a delivery based on distance 
  private double getDeliveryCost(int distance)
  {
    return distance * DELIVERYRATE;
  }

  private double getRideCost(int distance)
  {
    return distance * RIDERATE;
  }

  // Go through all drivers and see if one is available
  // Choose the first available driver
  // Return null if no available driver
  private Driver getAvailableDriver()
  {
    // Fill in the code
    for (Driver i: drivers) {
      if (i.getStatus() == Driver.Status.AVAILABLE) { // checks if the status of driver is available 
        return i; // returns the available driver
      }
    }
    return null; // returns null if no available drivers
  }

  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers()
  {
    System.out.println();
    
    for (int i = 0; i < users.size(); i++)
    {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      users.get(i).printInfo();
      System.out.println(); 
    }
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers()
  {
    // Fill in the code
    for (Driver i : drivers) {
      i.printInfo(); // print info of each driver
    }

  }

  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests()
  {
    // Fill in the code
    for (TMUberService i : serviceRequests) {
      i.printInfo(); // print info of each service request
    }
  }

  // Add a new user to the system
  public boolean registerNewUser(String name, String address, double wallet)
  {
    // Fill in the code. Before creating a new user, check paramters for validity
    // See the assignment document for list of possible erros that might apply
    // Write the code like (for example):
    // if (address is *not* valid)
    // {
    //    set errMsg string variable to "Invalid Address "
    //    return false
    // }
    // If all parameter checks pass then create and add new user to array list users
    // Make sure you check if this user doesn't already exist!
    if (name == null || name.equals("")) {
      errMsg = "Invalid User Name ";
      return false;
    }
    if (address == null || address.equals("")) {
      errMsg = "Invalid User Address ";
      return false;
    }
    if (wallet <= 0) {
      errMsg = "Invalid Money in Wallet ";
      return false;
    }
    for (User i: users) {
      if (i.getName().equals(name) && i.getAddress().equals(address)) {
        errMsg = "User Already Exists in System ";
        return false;
      }
    }
    userAccountId++;
    User newUser = new User(Integer.toString(userAccountId), name, address, wallet); // create new user 
    users.add(newUser); // add user to users
    return true;
  }

  // Add a new driver to the system
  public boolean registerNewDriver(String name, String carModel, String carLicencePlate)
  {
    // Fill in the code - see the assignment document for error conditions
    // that might apply. See comments above in registerNewUser
    if (name == null || name.equals("")) {
      errMsg = "Invalid Driver Name";
      return false;
    }
    if (carModel == null || carModel.equals("")) {
      errMsg = "Invalid Car Model";
      return false;
    }
    if (carLicencePlate == null || carLicencePlate.equals("")) {
      errMsg = "Invalid Car Licence Plate";
      return false;
    }
    for (Driver i : drivers) {
      if (i.getName().equals(name) && i.getCarModel().equals(carModel) && i.getLicensePlate().equals(carLicencePlate)) {
        errMsg = "Driver Already Exists in System";
        return false;
      }
    }
    driverId++;
    Driver newDriver = new Driver(Integer.toString(driverId), name, carModel, carLicencePlate); // create new driver
    drivers.add(newDriver); // add new driver to drivers
    return true;
  }

  // Request a ride. User wallet will be reduced when drop off happens
  public boolean requestRide(String accountId, String from, String to)
  {
    // Check for valid parameters 
	// Use the account id to find the user object in the list of users 
    // Get the distance for this ride 
    // Note: distance must be > 1 city block! 
    // Find an available driver 
    // Create the TMUberRide object 
    // Check if existing ride request for this user - only one ride request per user at a time! 
    // Change driver status 
    // Add the ride request to the list of requests 
    // Increment the number of rides for this user 
    if (accountId == null || accountId.equals("") || from == null || from.equals("") || to == null || to.equals("")) {
      errMsg = "User Account Not Found";
      return false;
    } 
    for (User i : users) {
      if (!(i.getAccountId().equals(accountId))) {
        errMsg = "User Account Not Found";
        return false;
      }
    }
    int distance = Math.abs(from.length()-to.length());
    if (distance <= 1) {
      errMsg = "Insufficient Travel Distance";
    }
    User user = getUser(accountId);
    for (TMUberService request : serviceRequests) { 
      if (request.getServiceType().equals("RIDE") && request.getUser().equals(user)) {
        errMsg = "User Already Has Ride Request";
        return false;
      }
    } 
    double cost = getRideCost(distance);
    Driver availableDriver = getAvailableDriver(); //find available driver 
    if (availableDriver == null) {
      errMsg = "No Drivers Available";
      return false;
    }
    availableDriver.setStatus(Driver.Status.DRIVING); // change status of driver to driving

    TMUberRide newRide = new TMUberRide(availableDriver, from, to, user, distance, cost); // create new ride
    serviceRequests.add(newRide); // add new ride to service requests
    user.addRide();  // increment rides for user
    return true;
  }


  // Request a food delivery. User wallet will be reduced when drop off happens
  public boolean requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId)
  {
    User user = getUser(accountId);
    for (TMUberService req : serviceRequests) {
      if (req instanceof TMUberDelivery && ((TMUberDelivery) req).getUser().equals(user) && ((TMUberDelivery) req).getRestaurant().equals(restaurant) && ((TMUberDelivery) req).getFoodOrderId().equals(foodOrderId)) {
        errMsg = "User Already Has Delivery Request at Restaraunt with this Food Order";
        return false;
      }
    }
    int distance = Math.abs(from.length() - to.length());
    double cost = getDeliveryCost(distance);
    Driver availableDriver = getAvailableDriver();
    if (availableDriver == null) { 
      errMsg = "No Drivers Available"; 
      return false; 
    }   
    TMUberDelivery newFoodDelivery = new TMUberDelivery(availableDriver, from, to, user, distance, cost, restaurant, foodOrderId); // create new food delivery 
    serviceRequests.add(newFoodDelivery); // add new food delivery to service requests 
    user.addDelivery();  // increment deliveries for user
    // See the comments above and use them as a guide
    // For deliveries, an existing delivery has the same user, restaurant and food order id
    // Increment the number of deliveries the user has had
    return true;
  }


  // Cancel an existing service request. 
  // parameter int request is the index in the serviceRequests array list
  public boolean cancelServiceRequest(int request)
  {
    // Check if valid request #
    // Remove request from list
    // Also decrement number of rides or number of deliveries for this user
    // since this ride/delivery wasn't completed
    if (request < 0 || request >= serviceRequests.size()) {
      errMsg = "Invalid Request #";
      return false;
    }
    TMUberService cancel = serviceRequests.get(request); // get request
    serviceRequests.remove(request); // remove request
    if (cancel instanceof TMUberRide) { // check if request is instance of TMUberRide
      ((TMUberRide) cancel).getUser().decrementRide(); // if it is then decrement ride
    } else if (cancel instanceof TMUberDelivery) { // check if request is instance of TMUberDelivery
      ((TMUberDelivery) cancel).getUser().decrementDelivery(); // if it is decrement deliveries
    } 
    return true;
  }
  
  // Drop off a ride or a delivery. This completes a service.
  // parameter request is the index in the serviceRequests array list
  public boolean dropOff(int request)
  {
    // See above method for guidance
    // Get the cost for the service and add to total revenues
    // Pay the driver
    // Deduct driver fee from total revenues
    // Change driver status
    // Deduct cost of service from user
    if (request < 0 || request >= serviceRequests.size()) {
      errMsg = "Invalid Request #";
      return false;
    }
    TMUberService dropService = serviceRequests.get(request);
    double cost = 0.0;
    if (dropService instanceof TMUberRide) {
      cost = ((TMUberRide) dropService).getDistance() * RIDERATE; // get cost of ride
    }else if (dropService instanceof TMUberDelivery) {
      cost = ((TMUberDelivery) dropService).getDistance() * DELIVERYRATE; // get cost of delivery 
    }
    totalRevenue += cost; // add cost to total revenues
    Driver driver = dropService.getDriver();// gets driver that was cancelled
    double driverPay = cost * PAYRATE; // driver pay
    driver.pay(driverPay); // pay the driver
    totalRevenue -= driverPay; // subtract the drivers pay from total revenue
    driver.setStatus(Driver.Status.AVAILABLE); // change driver status to available
    User user = dropService.getUser(); 
    user.payForService(cost);
    serviceRequests.remove(request);
    return true;
  }


  // Sort users by name
  // Then list all users
  public void sortByUserName()
  {
    users.sort(new NameComparator()); // sorts list of users by name
    listAllUsers(); // prints sorted users
  }

  // Helper class for method sortByUserName
  private class NameComparator implements Comparator<User> 
  {
    public int compare(User other1, User other2) {
      return other1.getName().compareTo(other2.getName()); //outputs -1 if other1 comes before other 2, and +1 for the other condition 
  }
  }

  // Sort users by number amount in wallet
  // Then ist all users
  public void sortByWallet()
  {
    users.sort(new UserWalletComparator()); // sorts users by amount in wallet
    listAllUsers();
  }
  // Helper class for use by sortByWallet
  private class UserWalletComparator implements Comparator<User> 
  {
    public int compare(User other1, User other2) {
      return Double.compare(other1.getWallet(), other2.getWallet()); //outputs -1 or +1 based on condition
    }
  }

  // Sort trips (rides or deliveries) by distance
  // Then list all current service requests
  public void sortByDistance()
  {
    Collections.sort(serviceRequests, new Comparator<TMUberService>() { // sorts service requests
      public int compare(TMUberService other1, TMUberService other2) { 
        return Integer.compare(other1.getDistance(), other2.getDistance()); // outputs -1 or +1 based on condition
      }
    });
    listAllServiceRequests();
  }

}

