package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;


    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }
    @BeforeEach
    private void setUpPerTest(){
        dataBasePrepareService.clearDataBaseEntries();
    }
    @AfterAll
    private static void tearDown(){

    }

    @Test
    void getNextAvailableSlotForCar() {
        ParkingType parkingType =ParkingType.CAR;
        int parkingSpotNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotNumber,ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 60 * 60 * 1000) );
        ticket.setVehicleRegNumber("Car");
        ticket.setInTime(inTime);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        assertEquals(1,ticketDAO.getTicket(ticket.getVehicleRegNumber()).getParkingSpot().getId());

    }
    @Test
    void getNextAvailableSlotForBike() {
        ParkingType parkingType =ParkingType.BIKE;
        int parkingSpotNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotNumber,ParkingType.BIKE,false);
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 60 * 60 * 1000) );
        ticket.setVehicleRegNumber("Bike");
        ticket.setInTime(inTime);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        assertEquals(4,ticketDAO.getTicket(ticket.getVehicleRegNumber()).getParkingSpot().getId());

    }

    @Test
    void updateParkingForCar() {
        ParkingType parkingType =ParkingType.CAR;
        ParkingSpot parkingSpot = new ParkingSpot(2,ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 60 * 60 * 1000) );
        ticket.setVehicleRegNumber("Car");
        ticket.setInTime(inTime);

        parkingSpotDAO.updateParking(parkingSpot);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);

        assertEquals(2,ticketDAO.getTicket(ticket.getVehicleRegNumber()).getParkingSpot().getId());
        assertNotEquals(true,ticketDAO.getTicket(ticket.getVehicleRegNumber()).getParkingSpot().isAvailable());
    }
    @Test
    void updateParkingForBike() {
        ParkingType parkingType =ParkingType.BIKE;
        ParkingSpot parkingSpot = new ParkingSpot(5,ParkingType.BIKE,false);
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 60 * 60 * 1000) );
        ticket.setVehicleRegNumber("Bike");
        ticket.setInTime(inTime);

        parkingSpotDAO.updateParking(parkingSpot);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);

        assertEquals(5,ticketDAO.getTicket(ticket.getVehicleRegNumber()).getParkingSpot().getId());
        assertNotEquals(true,ticketDAO.getTicket(ticket.getVehicleRegNumber()).getParkingSpot().isAvailable());
        assertEquals(parkingType.BIKE,ticketDAO.getTicket(ticket.getVehicleRegNumber()).getParkingSpot().getParkingType());
    }

}