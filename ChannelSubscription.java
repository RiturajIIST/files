/* 
THIS PROGRAM IS AVAILABLE TO RUN ONLINE AT:
https://replit.com/@devriturajkumar/Channel-Subscription-Service#ChannelSubscription.java

NAME : RITURAJ KUMAR

BRANCH: COMPUTER SCIENCE AND ENGINEERING
COURSE: BTECH
SEMESTER: 3
YEAR: SECOND
GROUP: CS-3

HTTPS://WWW.RITURAJKUMAR.COM

THIS FILE IS FOR CHANNEL SUBSCRIPTION SYSTEM (EXAMPLE)

GIVEN BY:- AMIT GOUD SIR (LAB CLASS)


***************** STEPS OF PROGRAM (ALGORITHM) *****************

STEP 1: SHOW WELCOME MESSAGE TO USER

STEP 2: ASK THE USER FOR EMAIL ADDRESS (VALIDATE CORRECT FORMAT)

STEP 3: ASK FOR PASSWORD (ACCEPT ANYTHING)

STEP 4: SHOW LOGIN SUCCESS IF EMAIL AND DUMMY PASSWORD MATCH

STEP 5: SHOW AVAILABLE CHANNELS TO SUBSCRIBE FROM LIST (MULTIPLE CAN BE SELECTED USING COMMA)

STEP 6: AFTER GETTING USER INPUT FOR WHICH CHANNELS TO SUBSCRIBE FROM, SHOW WHICH CHANNELS ARE GOING TO BE SUBSCRIBED

STEP 7: SHOW "AVAILABLE SUBSCRIPTION PLANS FOR CHANNELS"

STEP 8: SHOW WARNING MESSAGE FOR CONFIRMATION FOR PAYMENT

STEP 9: ASK THE USER FOR PAYMENT METHOD (UPI/CARD)

STEP 10: GET UPI ID AND VALIDATE OR GET DEBIT CARD NUMBER AS DECLARED FROM HASHKEY COLLECTION AND IF VALID SHOW SUCCESS MESSAGE

        UPI ID : ANY ID CONTAINING @ SYMBOL (EXAMPLE: RITURAJ@GATEWAY)
        UPI PIN: ANY NUMBER (VALIDATE IF LENGTH IS 6 DIGITS)

        DEBIT CARD:     NUMBERS:         PIN:
                    ("1111111111111111", 1111)
                    ("2222222222222222", 2222)
                    ("1212121212121212", 1212)
                    ("1231231231231231", 1231)
                    ("1234123412341234", 1234)
                    ("9764205318476092", 5432)
                    ("5432689170253186", 7890)
                    ("2609473858129364", 2109)
                    ("7081364592031875", 3456)
                    ("1956802346713890", 6543)


STEP 11: AFTER SUCCESSFUL PAYMENT, SHOW THE SUBSCRIPTION DETAILS LIKE:-
                    EMAIL ID USED FOR SUBSCRIPTION, 
                    SUBSCRIPTION PLAN, 
                    NUMBER OF CHANNELS SUBSCRIBED TO, 
                    SUBSCRIBED CHANNELS NAME, 
                    SUBSCRIPTION AMOUNT (WITHOUT TAX), 
                    TAXABLE AMOUNT, 
                    CURRENT MONTH, 
                    SUBSCRIPTION EXPIRY DATE, 
                    TIME OF SUBSCRIPTION ETC.

STEP 12: SHOW BILL WITH TAX AND BILL GENERATION TIME.

STEP 13. SAVE BILL AND SUBSCRIPTION DETAILS TO A TEXT FILE NAMED AS "EMAIL_BILL.TXT" SO WE CAN TRACK EACH USERS PLAN.

STEP 14: SHOW THANK YOU MESSAGE FOR SUBSCRIBING.

***************End of Program*****************

*/

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread;

public class ChannelSubscription {

  private static final Map<String, Integer> debitCardDatabase = generateDebitCardDatabase();
  private static final Set<String> subscribedChannels = new HashSet<>();
  private static final Map<String, String> userDatabase = new HashMap<>();
  private static final Map<Integer, String> subscriptionPlans = createSubscriptionPlans();
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("\nWelcome to Channel Subscription System !");
    
    System.out.println("Login and Choose From Below Services To Subscribe From=>");
     
        
   
    // Ask the user for email ID and password (dummy login)
    String email;
    do {
      System.out.print("\nEnter your E-mail ID: ");
      email = scanner.next();
    } while (!isValidEmail(email));

    System.out.print("\nEnter your Password: ");
    String password = scanner.next();
    userDatabase.put(email, password);
    
    // Dummy login (consider any email ID as a valid login)
    System.out.println("Login Successful !!");
    
    System.out.println("Welcome, " + capitalizeEmail(email) + "!");
    

    // Displaying the available channels
    String[] channels = {
        "Movie Channel", "Sports Channel", "News Channel", "Music Channel",
        "Educational Channel", "Kids Channel", "Language Learning Channel",
        "Cooking Channel", "DIY Channel", "Gardening Channel", "Beauty Channel",
        "Fitness Channel", "Meditation Channel", "ASMR Channel", "Gaming Channel",
        "Lifestyle Channel", "Comedy Channel", "Documentaries Channel", "Travel Channel",
        "Business Channel"
    };

    System.out.println("\nAvailable Channels:");
    for (int i = 0; i < channels.length; i++) {
      System.out.println((i + 1) + ". " + channels[i]);
    }

    // Ask the user to subscribe to channels
    String selectedChannelsInput;
    do {
      System.out.print("\nEnter the numbers of the channels you want to subscribe to (comma-separated): ");
      selectedChannelsInput = scanner.next();
    } while (!isValidChannelInput(selectedChannelsInput, channels.length));

    // Process each selected channel
    for (String selectedChannelIndexStr : selectedChannelsInput.split(",")) {
      int selectedChannelIndex;
      try {
        selectedChannelIndex = Integer.parseInt(selectedChannelIndexStr.trim());

        // Validate user input
        if (selectedChannelIndex < 1 || selectedChannelIndex > channels.length) {
          System.out.println("Invalid channel selection. Skipping.");
          continue;
        }

        String selectedChannel = channels[selectedChannelIndex - 1];

        // Check for duplicate channel selection
        if (!subscribedChannels.contains(selectedChannel)) {
          subscribedChannels.add(selectedChannel);
          System.out.println("You are going to subscribe to: " + selectedChannel);
        } else {
            
          System.out.println(selectedChannel + " is already Selected. Skipping.");
        }
      } catch (NumberFormatException e) {
        
        System.out.println("Invalid input format. Skipping.");
      }
    }

    // Ask for subscription plan
    int selectedPlan;
    do {
        
      System.out.println("\nAvailable Subscription Plans (per channel):");
      for (int planNumber : subscriptionPlans.keySet()) {
        System.out.println(planNumber + ". " + subscriptionPlans.get(planNumber));
      }
      
      System.out.print("Choose a subscription plan (1-4): ");
      while (!scanner.hasNextInt()) {
        System.out.println("Invalid input. Please enter a number between 1 and 4.");
        scanner.next();
      }
      selectedPlan = scanner.nextInt();
    } while (selectedPlan < 1 || selectedPlan > 4);

    // Display a warning message before payment
    displayWarningMessage(selectedPlan);

    // Ask the user for payment method
    String paymentMethod;
    do {
      System.out.print("Choose payment method (1: UPI, 2: Debit Card): ");
      paymentMethod = scanner.next();
    } while (!isValidPaymentMethod(paymentMethod));

    // Handle payment based on the selected method
    double subscriptionAmnt = calculateSubscriptionAmount(selectedPlan);
    double taxableAmnt = calculateTaxableAmount(selectedPlan);
    if (paymentMethod.equals("1")) {
      handleUpiPayment(email, subscriptionAmnt, taxableAmnt);
    } else {
      handleDebitCardPayment(email, subscriptionAmnt, taxableAmnt);
    }
     
    
    // Display the subscription details
    System.out.println("\n******************** Subscription Details ****************************************");
    System.out.println("Email ID: " + capitalizeEmail(email));
    
    System.out.println("Subscription Plan: " + subscriptionPlans.get(selectedPlan));
    
    System.out.println("Channels Subscribed: " + subscribedChannels.size());
    
    System.out.println("Subscribed Channels: " + String.join(", ", subscribedChannels));
    

    double subscriptionAmount = calculateSubscriptionAmount(selectedPlan);
    double taxableAmount = calculateTaxableAmount(selectedPlan);
    System.out.println("Subscription Amount: $" + subscriptionAmount);
    
    System.out.println("Taxable Amount: $" + taxableAmount);
    


    
    // Generate and display the bill
    generateAndDisplayBill(subscriptionAmount, taxableAmount);

    System.out.println("************************************************************");
    System.out.println("\nThank you for using our Channel Subscription service!\n");

    // Display the current month
    Calendar calendar = Calendar.getInstance();
    String currentMonth = new SimpleDateFormat("MMMM").format(calendar.getTime());
    System.out.println("Current Month: " + currentMonth);
    

    // Display the expiration date (30 days from now)
    calendar.add(Calendar.DAY_OF_MONTH, 30);
    String expirationDate = dateFormat.format(calendar.getTime());
    System.out.println("Subscription Expiration Date: " + expirationDate);
    

    // Display the time of subscription
    String subscriptionTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
    System.out.println("Subscription Time: " + subscriptionTime);
    

    // Save subscription details
    saveSubscriptionDetails(email, subscribedChannels, selectedPlan);

    // Generate and display the bill
    generateAndSaveBill(email, subscribedChannels, selectedPlan, subscriptionAmount, taxableAmount);
    
    System.out.println("************************************************************");
    System.out.println("\nThank you for using our Channel Subscription service!\n");
    scanner.close();
  }
  private static void saveSubscriptionDetails(String userEmail, Set<String> channels, int selectedPlan) {
    String fileName = userEmail + "_bill.txt";
    try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
      writer.println("Subscription Details:");
      writer.println("Email ID: " + capitalizeEmail(userEmail));
      for (String channel : channels) {
        writer.println(channel + " Subscription Plan: " + subscriptionPlans.get(selectedPlan));
      }
      System.out.println("Subscription details appended to " + fileName);
    } catch (IOException e) {
      System.out.println("Error saving subscription details: " + e.getMessage());
    }
  }

  private static void generateAndSaveBill(String userEmail, Set<String> channels, int selectedPlan,
  double subscriptionAmount, double taxableAmount) {
String fileName = userEmail + "_bill.txt";
try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
  writer.println("Bill and Subscription Details:");
  writer.println("Email ID: " + capitalizeEmail(userEmail));
  for (String channel : channels) {
    double gst = taxableAmount * 0.28;
    double totalAmount = subscriptionAmount + gst;

    writer.println("Channel: " + channel);
    writer.println("Subscription Plan: " + subscriptionPlans.get(selectedPlan));
    writer.println("Subscription Amount: $" + subscriptionAmount);
    writer.println("Taxable Amount: $" + taxableAmount);
    writer.println("GST (28%): $" + gst);
    writer.println("Total Amount (including GST): $" + totalAmount);
    writer.println("Time of Bill Generation: " + dateFormat.format(new Date()));
    writer.println("************************************************************");
  }
  System.out.println("Bill and subscription details saved in " + fileName);
} catch (IOException e) {
  System.out.println("Error saving bill and subscription details: " + e.getMessage());
}
}

  // Validate user input for selected channels
  private static boolean isValidChannelInput(String input, int maxChannel) {
    String[] selectedChannelsArray = input.split(",");
    for (String selectedChannelIndexStr : selectedChannelsArray) {
      try {
        int selectedChannelIndex = Integer.parseInt(selectedChannelIndexStr.trim());
        if (selectedChannelIndex < 1 || selectedChannelIndex > maxChannel) {
          System.out.println("Invalid channel selection. Please enter valid channel numbers.");
          return false;
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input format. Please enter valid channel numbers.");
        return false;
      }
    }
    return true;
  }

  // Validate user input for payment method
  private static boolean isValidPaymentMethod(String input) {
    if (!input.equals("1") && !input.equals("2")) {
      System.out.println("Invalid payment method. Please enter 1 for UPI or 2 for Debit Card.");
      return false;
    }
    return true;
  }

  // Validate user input for email format
  private static boolean isValidEmail(String email) {
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    if (!email.matches(emailRegex)) {
      System.out.println("Invalid email format. Please enter a valid email address.");
      return false;
    }
    return true;
  }

  // Display a warning message before payment
  private static void displayWarningMessage(int selectedPlan) {
    
    System.out.println("\n************************************************************");
    System.out.println("                   ** WARNING **                     ");
    System.out.println("************************************************************");
    System.out.println("You are about to pay for the following subscription:");
    System.out.println("Subscription Plan: " + subscriptionPlans.get(selectedPlan));
    double amountToBePaid = calculateSubscriptionAmount(selectedPlan);
    System.out.println("Amount to be paid: $" + amountToBePaid);
    System.out.println("Please ensure that your payment details are correct.");
    System.out.println("************************************************************\n");
  }

  // Handle UPI payment
  private static void handleUpiPayment(String email, double subscriptionAmnt, double taxableAmnt) {
    Scanner scanner = new Scanner(System.in);

    // Ask for UPI ID
    System.out.print("Enter your UPI ID: ");
    String upiId = scanner.next();

    // Ask for UPI PIN
    int upiPin;
    do {
        System.out.print("Enter your UPI PIN (4 digits): ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a 4-digit UPI PIN.");
            scanner.next();
        }
        upiPin = scanner.nextInt();
    } while (upiPin < 0000 || upiPin > 9999);

        double gst = taxableAmnt * 0.28;
        double totalAmnt = subscriptionAmnt + gst;

    // Display payment success message
    
    System.out.println("Payment successful via UPI. $" + totalAmnt + " is deducted from your " + upiId + "UPI linked bank account.");
    scanner.close();
}
    // Handle Debit Card payment
private static void handleDebitCardPayment(String email, double subscriptionAmnt, double taxableAmnt) {
  Scanner scanner = new Scanner(System.in);

  // Ask for Debit Card details
  System.out.print("Enter Debit Card Number: ");
  String cardNumber = scanner.next();

  int maxAttempts = 3;
  int pinAttempts;
  int pin;
  boolean pinVerified = false;

  do {
      pinAttempts = 0;

      // Loop to ask for PIN with a maximum number of attempts
      while (pinAttempts < maxAttempts) {
          System.out.print("Enter Debit Card PIN (4 digits): ");
          while (!scanner.hasNextInt()) {
              System.out.println("Invalid input. Please enter a 4-digit PIN.");
              scanner.next();
          }
          pin = scanner.nextInt();

          double gst = taxableAmnt * 0.28;
          double totalAmnt = subscriptionAmnt + gst;

          // Verify Debit Card details
          if (verifyDebitCard(cardNumber, pin)) {
              pinVerified = true;
              System.out.println("Payment of $" + totalAmnt + " is successful via Debit Card. ");
              break;
          } else {
              pinAttempts++;
              System.out.println("Incorrect PIN. Attempts remaining: " + (maxAttempts - pinAttempts));
          }
      }

      if (!pinVerified) {
          System.out.println("Maximum PIN attempts reached. Please enter Debit Card Number again.");
          System.out.print("Enter Debit Card Number: ");
          cardNumber = scanner.next();
      }

  } while (!pinVerified);

  scanner.close();
}

  // Calculate subscription amount based on the selected plan
  private static double calculateSubscriptionAmount(int selectedPlan) {
    switch (selectedPlan) {
      case 1:
        return 2.0 * subscribedChannels.size();
      case 2:
        return 4.0 * subscribedChannels.size();
      case 3:
        return 7.0 * subscribedChannels.size();
      case 4:
        return 10.0 * subscribedChannels.size();
      default:
        return 0.0;
    }
  }

  // Calculate taxable amount based on the selected plan
  private static double calculateTaxableAmount(int selectedPlan) {
    switch (selectedPlan) {
      case 1:
        return 2.0 * subscribedChannels.size();
      case 2:
        return 4.0 * subscribedChannels.size();
      case 3:
        return 7.0 * subscribedChannels.size();
      case 4:
        return 10.0 * subscribedChannels.size();
      default:
        return 0.0;
    }
  }

  // Capitalize the first letter of the email ID
  private static String capitalizeEmail(String email) {
    String[] parts = email.split("@");
    parts[0] = parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1);
    return String.join("@", parts);
  }

  // Verify debit card details
  private static boolean verifyDebitCard(String cardNumber, int pin) {

    // Remove any spaces or non-digit characters from the cardNumber
    String formattedCardNumber = cardNumber.replaceAll("\\D", "");

    // Check if the formatted card number is in the database
    return debitCardDatabase.containsKey(formattedCardNumber) && debitCardDatabase.get(formattedCardNumber) == pin;

  }

  // Generate a dummy database of debit card numbers and pins
  private static Map<String, Integer> generateDebitCardDatabase() {
    Map<String, Integer> dummyDebitCardDatabase = new HashMap<>();
    dummyDebitCardDatabase.put("1111111111111111", 1111);
    dummyDebitCardDatabase.put("2222222222222222", 2222);
    dummyDebitCardDatabase.put("1212121212121212", 1212);
    dummyDebitCardDatabase.put("1231231231231231", 1231);
    dummyDebitCardDatabase.put("1234123412341234", 1234);
    dummyDebitCardDatabase.put("9764205318476092", 5432);
    dummyDebitCardDatabase.put("5432689170253186", 7890);
    dummyDebitCardDatabase.put("2609473858129364", 2109);
    dummyDebitCardDatabase.put("7081364592031875", 3456);
    dummyDebitCardDatabase.put("1956802346713890", 6543);
    return dummyDebitCardDatabase;
  }

  // Create a map of subscription plans
  private static Map<Integer, String> createSubscriptionPlans() {
    Map<Integer, String> plans = new HashMap<>();
    plans.put(1, "Basic ($2.0 per channel)");
    plans.put(2, "Advanced ($4.0 per channel)");
    plans.put(3, "Pro ($7.0 per channel)");
    plans.put(4, "Premium ($10.0 per channel)");
    return plans;
  }

  // Generate and display the bill
  private static void generateAndDisplayBill(double subscriptionAmount, double taxableAmount) {
    double gst = taxableAmount * 0.28;
    double totalAmount = subscriptionAmount + gst;
    
    System.out.println("\n************************************************************");
    System.out.println("                              BILL                                ");
    System.out.println("************************************************************");
    System.out.println("Subscription Amount: $" + subscriptionAmount);
    System.out.println("Taxable Amount: $" + taxableAmount);
    System.out.println("GST (28%): $" + gst);
    System.out.println("Total Amount (including GST): $" + totalAmount);
    System.out.println("Time of Bill Generation: " + dateFormat.format(new Date()));
    System.out.println("************************************************************\n");
  }
  // // Wait method
  //  private static void wait(int ms) {
  //   try 
  //   {
  //        Thread.sleep(ms);
  //   } catch (InterruptedException e) {
  //        System.out.println("Something Went Wrong !!");
  //           }
  // }
}
