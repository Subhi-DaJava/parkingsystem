package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.commons.math3.util.Precision;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class ParkingService {

    private static final Logger logger = LogManager.getLogger("ParkingService");

    private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

    private InputReaderUtil inputReaderUtil;
    private ParkingSpotDAO parkingSpotDAO;
    private  TicketDAO ticketDAO;

    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO){
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    public void processIncomingVehicle() {
        try{
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if(parkingSpot !=null && parkingSpot.getId() > 0){

                String vehicleRegNumber = getVehicleRegNumber();


                boolean isAlreadyParked = ticketDAO.isVehicleAlreadyParked(vehicleRegNumber);
                if (isAlreadyParked) {
                    System.out.println("This vehicle, registration number " +vehicleRegNumber+" is parking now.\n");
                    return;
                }
                //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
                //ticket.setId(ticketID);
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);//allow this parking space and mark its availability as false
                Date inTime = new Date();
                Ticket ticket = new Ticket();

                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0.0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                if(ticketDAO.isVehicleRecurrent(vehicleRegNumber)){
                    System.out.println("Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount.");
                }
                ticketDAO.saveTicket(ticket);
                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number: "+parkingSpot.getId());
                System.out.println("Recorded in-time for vehicle number: "+vehicleRegNumber+" is:"+inTime+"\n");

            }
        }catch(Exception e){
            logger.error("Unable to process incoming vehicle",e);
        }
    }

    /**
     * This method ask the client to type the reg number of his vehicle
     * @return vehicle register number
     * @throws Exception when the reg number is null
     */
    private String getVehicleRegNumber() throws Exception {
        System.out.println("Please type the vehicle registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    /**
     * This method check and return the availability of the parking spot
     * in the database when the client enter the type of the vehicle
     * @return ParkingSpot
     */
    public ParkingSpot getNextParkingNumberIfAvailable(){
        int parkingNumber; // parkingNumber = 0;
        ParkingSpot parkingSpot = null;
        try{
            ParkingType parkingType = getVehicleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if(parkingNumber > 0){
                parkingSpot = new ParkingSpot(parkingNumber,parkingType, true);
            }else{
                throw new Exception("Error fetching parking number from DB. Parking slots might be full");
            }
        }catch(IllegalArgumentException ie){
            logger.error("Error parsing user input for type of vehicle", ie);
        }catch(Exception e){
            logger.error("Error fetching next available parking slot", e);
        }
        return parkingSpot;
    }

    /**
     * This method ask the client the type of his vehicle
     * @return ParkingType
     */
    public ParkingType getVehicleType(){
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch(input){
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    /**
     *
     */
    public void processExitingVehicle() {
        try{
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);

            boolean isVehicleParked = ticketDAO.isVehicleAlreadyParked(vehicleRegNumber);
            if(!isVehicleParked) {
                System.out.println("This vehicle is not parked here yet.\n");
            return ;
            }
            Date outTime = new Date();
            ticket.setOutTime(outTime);

            fareCalculatorService.calculateFare(ticket);
            //Check if the vehicle is recurring or not and if it is, calcul the price with 5% discount

            if(ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare: " + ticket.getPrice()+", parking time is : "
                        + Precision.round(((double)ticket.getOutTime().getTime() / (1000 * 60 * 60 ) - (double) ticket.getInTime().getTime() / (1000 * 60 * 60 )), 2)+" hours.");
                System.out.println("Recorded out-time for vehicle number: " + ticket.getVehicleRegNumber() + " is: " + outTime+"\n");
            }else{
                System.out.println("Unable to update ticket information. Error occurred");
            }
        }catch(Exception e){
            logger.error("Unable to process exiting vehicle",e);
        }
    }


}
