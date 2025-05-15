// Name: Saadia Ali
//Student ID: 501227915
import java.util.Arrays;
import java.util.Scanner;

// The city consists of a grid of 9 X 9 City Blocks

// Streets are east-west (1st street to 9th street)
// Avenues are north-south (1st avenue to 9th avenue)

// Example 1 of Interpreting an address:  "34 4th Street"
// A valid address *always* has 3 parts.
// Part 1: Street/Avenue residence numbers are always 2 digits (e.g. 34).
// Part 2: Must be 'n'th or 1st or 2nd or 3rd (e.g. where n => 1...9)
// Part 3: Must be "Street" or "Avenue" (case insensitive)

// Use the first digit of the residence number (e.g. 3 of the number 34) to determine the avenue.
// For distance calculation you need to identify the the specific city block - in this example 
// it is city block (3, 4) (3rd avenue and 4th street)

// Example 2 of Interpreting an address:  "51 7th Avenue"
// Use the first digit of the residence number (i.e. 5 of the number 51) to determine street.
// For distance calculation you need to identify the the specific city block - 
// in this example it is city block (7, 5) (7th avenue and 5th street)
//
// Distance in city blocks between (3, 4) and (7, 5) is then == 5 city blocks
// i.e. (7 - 3) + (5 - 4) 

public class CityMap
{
  // Checks for string consisting of all digits
  // An easier solution would use String method matches()
  private static boolean allDigits(String s)
  {
    for (int i = 0; i < s.length(); i++)
      if (!Character.isDigit(s.charAt(i)))
        return false;
    return  true;
  }

  // Get all parts of address string
  // An easier solution would use String method split()
  // Other solutions are possible - you may replace this code if you wish
  private static String[] getParts(String address)
  {
    String parts[] = new String[3];
    
    if (address == null || address.length() == 0)
    {
      parts = new String[0];
      return parts;
    }
    int numParts = 0;
    Scanner sc = new Scanner(address);
    while (sc.hasNext())
    {
      if (numParts >= 3)
        parts = Arrays.copyOf(parts, parts.length+1);

      parts[numParts] = sc.next();
      numParts++;
    }
    if (numParts == 1)
      parts = Arrays.copyOf(parts, 1);
    else if (numParts == 2)
      parts = Arrays.copyOf(parts, 2);
    return parts;
  }

  // Checks for a valid address
  public static boolean validAddress(String address)
  {
    // Fill in the code
    // Make use of the helper methods above if you wish
    // There are quite a few error conditions to check for 
    // e.g. number of parts != 3
    String[] parts = getParts(address); // initializes string array called parts that contains parts of the address
    if (parts.length != 3) { // checks that if parts length is not equal to 3, return false
      return false;
    }
    
    if (parts[0].length() != 2) { //checks that street/avenue residence numbers are not 2 digits, return false
      return false;
    }
    // Part 2: Must be 'n'th or 1st or 2nd or 3rd (e.g. where n => 1...9)
    String nth = parts[1];
    if (!(nth.equals("1st") || nth.equals("2nd") || nth.equals("3rd") || nth.equals("4th") || nth.equals("5th") || nth.equals("6th") || nth.equals("7th") || nth.equals("8th") || nth.equals("9th"))) {
      return false;
    }
    // Part 3: Must be "Street" or "Avenue" (case insensitive)
    if (!(parts[2].equals("Street") || parts[2].equals("Avenue"))) {
      return false;
    }
    
    // Use the first digit of the residence number (e.g. 3 of the number 34) to determine the avenue.
    // For distance calculation you need to identify the the specific city block - in this example 
    // it is city block (3, 4) (3rd avenue and 4th street)
    int resNum = Integer.parseInt(parts[0]);
    int avenue = resNum / 10; 
    String extract = parts[1].substring(0, parts[1].length()-2);
    int street = Integer.parseInt(extract);
    return true; 
  } 

  // Computes the city block coordinates from an address string
  // returns an int array of size 2. e.g. [3, 4] 
  // where 3 is the avenue and 4 the street
  // See comments at the top for a more detailed explanation
  public static int[] getCityBlock(String address)
  {
    int[] block = {-1, -1}; 
    // Fill in the code
    if (!validAddress(address)) { // checks if not valid address
      System.err.println("Invalid Address");
      return block;
    }
    String[] parts = getParts(address); // initializes string array that contains the parts of the address
    int resNum = Integer.parseInt(parts[0]); // gets residence number and converts from string to int
    int street = resNum % 10; // modules 10, to get 2nd digit
    String extract = parts[1].substring(0, parts[1].length()-2); //gets the first digit on the second number 
    int avenue = Integer.parseInt(extract); // converts the string extract to int
    block[0] = avenue; // assigns avenue to block[0]
    block[1] = street; // assigns street to block[1]
    return block; 
  }
  
  // Calculates the distance in city blocks between the 'from' address and 'to' address
  // Hint: be careful not to generate negative distances
  
  // This skeleton version generates a random distance
  // If you do not want to attempt this method, you may use this default code
  public static int getDistance(String from, String to)
  {
    // Fill in the code or use this default code below. If you use
    // the default code then you are not eligible for any marks for this part
    int[] fromBlock = getCityBlock(from); // gets block(int array) for the from address 
    int[] toBlock = getCityBlock(to); // gets block(int array) for the to address 
    if (fromBlock[0] == -1 || fromBlock[1] == -1 || toBlock[0] == -1 || toBlock[1] == -1) { // checks if any of the blocks contains -1, which means invalid address
      System.err.println("Invalid address"); 
    } 
    int distance = Math.abs(fromBlock[0] - toBlock[0]) + Math.abs(fromBlock[1] - toBlock[1]); // calculates distance in city blocks
    return distance; 
    // Math.random() generates random number from 0.0 to 0.999
    // Hence, Math.random()*17 will be from 0.0 to 16.999
    ///double doubleRandomNumber = Math.random() * 17;
    // cast the double to whole number
    ///int randomNumber = (int)doubleRandomNumber;
    ///return (randomNumber);
  }
}
