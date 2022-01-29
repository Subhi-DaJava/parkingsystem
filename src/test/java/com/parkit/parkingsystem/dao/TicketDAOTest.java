package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.ParkingDataBaseIT;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TicketDAOTest {

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
    void saveTicketForCarForOneHourParking() {

        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 60 * 60 * 1000) );
        ticket.setVehicleRegNumber("Car");
        ticket.setInTime(inTime);
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR),ParkingType.CAR,true);
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(1.5);
        ticketDAO.saveTicket(ticket);

        assertNotNull(ticketDAO.getTicket("Car").getOutTime());
        assertEquals(1,parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));

    }
    @Test
    void saveTicketForBikeForOneHourParking() {

        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 60 * 60 * 1000) );
        ticket.setVehicleRegNumber("Bike");
        ticket.setInTime(inTime);
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE),ParkingType.BIKE,true);
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(1.0);
        ticketDAO.saveTicket(ticket);

        assertNotNull(ticketDAO.getTicket("Bike").getOutTime());
        assertEquals(4,parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));

    }
    @Test
    void saveTicketForCarIsEnteringInTheParking() {

        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis());
        ticket.setVehicleRegNumber("Car");
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR),ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        parkingSpotDAO.updateParking(parkingSpot);
        ticketDAO.saveTicket(ticket);

        assertNull(ticketDAO.getTicket("Car").getOutTime());
        assertEquals(0.0,ticketDAO.getTicket("Car").getPrice());
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    void saveTicketForBikeIsEnteringInTheParking() {

        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis());
        ticket.setVehicleRegNumber("Bike");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE), ParkingType.BIKE, false);
        ticket.setParkingSpot(parkingSpot);
        parkingSpotDAO.updateParking(parkingSpot);
        ticketDAO.saveTicket(ticket);

        assertNull(ticketDAO.getTicket("Bike").getOutTime());
        assertEquals(0.0, ticketDAO.getTicket("Bike").getPrice());
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));

    }
    @Test
    void getTicketForCar() {
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis());
        ticket.setVehicleRegNumber("Car");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR),ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        parkingSpotDAO.updateParking(parkingSpot);
        ticketDAO.saveTicket(ticket);
        Ticket ticketNew = ticketDAO.getTicket(ticket.getVehicleRegNumber());

        assertEquals("Car",ticketNew.getVehicleRegNumber());
        assertEquals(0.0, ticketNew.getPrice());
        assertNull(ticketNew.getOutTime());
        assertEquals(1,ticketNew.getParkingSpot().getId());

    }

    @Test
    void getTicketForBike() {
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis());
        ticket.setVehicleRegNumber("Bike");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE),ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);
        parkingSpotDAO.updateParking(parkingSpot);
        ticketDAO.saveTicket(ticket);
        Ticket ticketNew = ticketDAO.getTicket(ticket.getVehicleRegNumber());

        assertEquals("Bike",ticketNew.getVehicleRegNumber());
        assertEquals(0.0, ticketNew.getPrice());
        assertNull(ticketNew.getOutTime());
        assertEquals(4,ticketNew.getParkingSpot().getId());

    }

    @Test
    void updateTicketForCar() {
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Car");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR),ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        parkingSpotDAO.updateParking(parkingSpot);

        ticketDAO.saveTicket(ticket);
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ticket.setPrice(1.5);
        ticket.setOutTime(outTime);

        ticketDAO.updateTicket(ticket);

        assertEquals(1.5,ticketDAO.getTicket("CAR").getPrice());
        assertNotNull(ticketDAO.getTicket("CAR").getOutTime());
    }
    @Test
    void updateTicketForBike() {
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Bike");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE),ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);
        parkingSpotDAO.updateParking(parkingSpot);

        ticketDAO.saveTicket(ticket);
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ticket.setPrice(1);
        ticket.setOutTime(outTime);

        ticketDAO.updateTicket(ticket);

        assertEquals(1,ticketDAO.getTicket("Bike").getPrice());
        assertNotNull(ticketDAO.getTicket("Bike").getOutTime());
    }

    @Test
    void isTheBikeRecurrent() {
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Bike");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE),ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);
        parkingSpotDAO.updateParking(parkingSpot);
        ticketDAO.saveTicket(ticket);

        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ticket.setPrice(1);
        ticket.setOutTime(outTime);
        ticketDAO.updateTicket(ticket);

        ticketDAO.isVehicleRecurrent("BikeOne");

        assertNotEquals("BikeOne",ticketDAO.getTicket("Bike").getVehicleRegNumber());
        assertTrue(ticketDAO.getTicket("Bike").getPrice() > 0);
        assertNotNull(ticketDAO.getTicket("Bike").getOutTime());

    }
    @Test
    void isTheCarRecurrent() {
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Car");
        ticket.setId(1);
        ticket.setInTime(inTime);

        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR),ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        parkingSpotDAO.updateParking(parkingSpot);
        ticketDAO.saveTicket(ticket);

        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ticket.setPrice(1.5);
        ticket.setOutTime(outTime);
        ticketDAO.updateTicket(ticket);

        ticketDAO.isVehicleRecurrent("CarOne");

        assertNotEquals("CarOne",ticketDAO.getTicket("Car").getVehicleRegNumber());
        assertTrue(ticketDAO.getTicket("Car").getPrice() > 0);
        assertNotNull(ticketDAO.getTicket("Car").getOutTime());

    }

    @Test
    void isTheCarAlreadyParked() {
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis()-60*60*1000);
        ticket.setVehicleRegNumber("Car");
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR),ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        parkingSpotDAO.updateParking(parkingSpot);
        ticketDAO.saveTicket(ticket);

        ticketDAO.isVehicleAlreadyParked(ticket.getVehicleRegNumber());

        assertEquals("Car",ticketDAO.getTicket("Car").getVehicleRegNumber());
        assertThat(ticketDAO.getTicket(ticket.getVehicleRegNumber()).getOutTime()).isNull();
    }
    @Test
    void isTheBikeAlreadyParked() {
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis()-60*60*1000);
        ticket.setVehicleRegNumber("Bike");
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR),ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        parkingSpotDAO.updateParking(parkingSpot);
        ticketDAO.saveTicket(ticket);

        ticketDAO.isVehicleAlreadyParked(ticket.getVehicleRegNumber());

        assertEquals("Bike",ticketDAO.getTicket("Bike").getVehicleRegNumber());
        assertThat(ticketDAO.getTicket(ticket.getVehicleRegNumber()).getInTime()).isNotNull();
    }
}