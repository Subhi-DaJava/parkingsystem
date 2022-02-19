package com.parkit.parkingsystem.util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;

/**
 * @author Tek and Subhi
 * Allows entering(to input) from keyboard and registering the information using the Scanner class
 * */
public class InputReaderUtil {

    private Scanner scan;
    public InputReaderUtil(){
        this.scan = new Scanner(System.in,"UTF-8");
    }
    public void setScan(Scanner scan) {
        this.scan = scan;
    }
    private static final Logger logger = LogManager.getLogger("InputReaderUtil");

    /**
     *
     * @return the number of selection : 1.processIncomingVehicle, 2.processExitingVehicle or 3.quit the system
     * or return the number of selection : 1.ParkingType.CAR, 2.ParkingType.BIKE or any other input (Incorrect input provided)
     * @throws Exception for any other option
     */
    public int readSelection() {
        try {
            return Integer.parseInt(scan.nextLine());
        }catch(Exception e){
            logger.error("Error while reading user input from Shell");
            System.out.println("Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    /**
     *
     * @return the vehicle number with String type
     * @throws Exception if the input is null
     */
    public String readVehicleRegistrationNumber() {
        try {
            String vehicleRegNumber= scan.nextLine();
            if(vehicleRegNumber == null || vehicleRegNumber.trim().length()==0 ) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        }catch(Exception e){
            logger.error("Error while reading user input from Shell");
            System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }


}
