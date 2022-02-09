package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InteractiveShell {
    private static final Logger logger = LogManager.getLogger("InteractiveShell");
    private static InputReaderUtil inputReaderUtil = new InputReaderUtil();
    private static ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();

    public static void setInputReaderUtil(InputReaderUtil inputReaderUtil) {
        InteractiveShell.inputReaderUtil = inputReaderUtil;
    }

    public static void setParkingSpotDAO(ParkingSpotDAO parkingSpotDAO) {
        InteractiveShell.parkingSpotDAO = parkingSpotDAO;
    }

    public static void setTicketDAO(TicketDAO ticketDAO) {
        InteractiveShell.ticketDAO = ticketDAO;
    }

    public static void setParkingService(ParkingService parkingService) {
        InteractiveShell.parkingService = parkingService;
    }

    private static TicketDAO ticketDAO = new TicketDAO();
    private static ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    public InteractiveShell(InputReaderUtil inputReaderUtil,ParkingSpotDAO parkingSpotDAO,TicketDAO ticketDAO,ParkingService parkingService){
        InteractiveShell.inputReaderUtil = new InputReaderUtil();
        InteractiveShell.parkingSpotDAO = new ParkingSpotDAO();
        InteractiveShell.ticketDAO = new TicketDAO();
        InteractiveShell.parkingService = new ParkingService(inputReaderUtil,parkingSpotDAO,ticketDAO);
    }

    public static void loadInterface() throws Exception {
        logger.info("App initialized!!!");
        System.out.println("Welcome to Parking System!");

        boolean continueApp = true;

        while(continueApp){
            loadMenu();
            int option = inputReaderUtil.readSelection();
            switch(option){
                case 1: {
                    parkingService.processIncomingVehicle();
                    break;
                }
                case 2: {
                    parkingService.processExitingVehicle();
                    break;
                }
                case 3: {
                    System.out.println("Exiting from the system!");
                    continueApp = false;
                    break;
                }
                default: System.out.println("Unsupported option. Please enter a number corresponding to the provided menu");
            }
        }
    }

    private static void loadMenu(){
        System.out.println("Please select an option. Simply enter the number to choose an action");
        System.out.println("1 New Vehicle Entering - Allocate Parking Space");
        System.out.println("2 Vehicle Exiting - Generate Ticket Price");
        System.out.println("3 Shutdown System");
    }

}
