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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tester les méthodes de lc classe TicketDAO
 */
class TicketDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeAll
    private static void setUp() {
        try {
            parkingSpotDAO = new ParkingSpotDAO();
            parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
            ticketDAO = new TicketDAO();
            ticketDAO.dataBaseConfig = dataBaseTestConfig;
            dataBasePrepareService = new DataBasePrepareService();
        }catch (Exception e){
            e.printStackTrace();
        }
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
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 60 * 60 * 1000) );
        ticket.setVehicleRegNumber("Car");
        ticket.setInTime(inTime);
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(1.5);
        //WHEN
        ticketDAO.saveTicket(ticket);
        //THEN
        assertNotNull(ticketDAO.getTicket("Car").getOutTime());
        assertEquals(1.5,ticketDAO.getTicket(ticket.getVehicleRegNumber()).getPrice());
    }
    @Test
    void saveTicketForBikeForOneHourParking() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 60 * 60 * 1000) );
        ticket.setVehicleRegNumber("Bike");
        ticket.setInTime(inTime);
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ParkingSpot parkingSpot = new ParkingSpot(4,ParkingType.BIKE,true);
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(1.0);
        //WHEN
        ticketDAO.saveTicket(ticket);
        //THEN
        assertNotNull(ticketDAO.getTicket("Bike").getOutTime());
        assertEquals(1.0,ticketDAO.getTicket(ticket.getVehicleRegNumber()).getPrice());


    }
    @Test
    void saveTicketForCarEnteringInTheParking() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis());
        ticket.setVehicleRegNumber("Car");
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setPrice(0.0);
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        //WHEN
        ticketDAO.saveTicket(ticket);
        //THEN
        assertNull(ticketDAO.getTicket("Car").getOutTime());
        assertEquals(0.0,ticketDAO.getTicket("Car").getPrice());
        assertEquals(1,ticketDAO.getTicket("CAR").getParkingSpot().getId());

    }

    @Test
    void saveTicketForBikeEnteringInTheParking() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis());
        ticket.setVehicleRegNumber("Bike");
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
        ticket.setParkingSpot(parkingSpot);
        //WHEN
        ticketDAO.saveTicket(ticket);
        //THEN
        assertNull(ticketDAO.getTicket("Bike").getOutTime());
        assertEquals(0.0, ticketDAO.getTicket("Bike").getPrice());
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));

    }

    @Test
    void saveTicketFailureForCarEnteringInTheParking() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis());
        ticket.setVehicleRegNumber("Car");
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(0,ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        //WHEN
        ticketDAO.saveTicket(ticket);
        //THEN
        assertFalse(ticketDAO.saveTicket(ticket));
    }

    @Test
    void getTicketForCar() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis());
        ticket.setVehicleRegNumber("Car");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        //WHEN
        Ticket ticketNew = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        //THEN
        assertEquals("Car",ticketNew.getVehicleRegNumber());
        assertEquals(0.0, ticketNew.getPrice());
        assertNull(ticketNew.getOutTime());
        assertEquals(1,ticketNew.getParkingSpot().getId());

    }

    @Test
    void getTicketForBike() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis());
        ticket.setVehicleRegNumber("Bike");
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(4,ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        //WHEN
        Ticket ticketNew = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        //THEN
        assertEquals("Bike",ticketNew.getVehicleRegNumber());
        assertEquals(0.0, ticketNew.getPrice());
        assertNull(ticketNew.getOutTime());
        assertEquals(4,ticketNew.getParkingSpot().getId());
    }

    @Test
    void updateTicketForCar() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Car");
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        Date outTime = new Date();
        ticket.setId(1);
        ticket.setOutTime(outTime);
        ticket.setPrice(1.5);
        //WHEN
        boolean updated = ticketDAO.updateTicket(ticket);
        //THEN
        assertTrue(updated);
    }
    @Test
    void updateTicketForBike() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Bike");
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(4,ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ticket.setPrice(1);
        //WHEN
        ticketDAO.updateTicket(ticket);
        //THEN
        assertTrue(ticketDAO.updateTicket(ticket));
    }

    @Test
    void noUpdateTicketForCar() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Car");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        //WHEN
        ticketDAO.updateTicket(ticket);
        //THEN
        assertFalse(ticketDAO.updateTicket(ticket));

    }
    @Test
    void noUpdateTicketForBike() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Bike");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);
        //WHEN
        ticketDAO.updateTicket(ticket);
        //THEN
        assertFalse(ticketDAO.updateTicket(ticket));

    }

    @Test
    void isTheBikeRecurrent() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Bike");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(4,ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ticket.setPrice(1);
        ticket.setOutTime(outTime);
        ticketDAO.updateTicket(ticket);
        //WHEN
        boolean recurrent = ticketDAO.isVehicleRecurrent("Bike");
        //THEN
        assertTrue(recurrent);
        assertNotNull(ticketDAO.getTicket("Bike").getOutTime());

    }
    @Test
    void isTheCarRecurrent() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Car");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        ticket.setPrice(1.5);
        ticket.setOutTime(outTime);
        ticketDAO.updateTicket(ticket);
        //WHEN
        boolean recurrent = ticketDAO.isVehicleRecurrent("Car");
        //THEN
        assertEquals("Car",ticketDAO.getTicket("Car").getVehicleRegNumber());
        assertTrue(recurrent);
        assertNotNull(ticketDAO.getTicket("Car").getOutTime());

    }

    @Test
    void isTheVehicleBikeNotRecurrent() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Bike");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(4,ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        //WHEN
        boolean recurrent = ticketDAO.isVehicleRecurrent("Bike");
        //THEN
        assertFalse(recurrent);
    }

    @Test
    void isTheVehicleCarNotRecurrent() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Car");
        ticket.setId(1);
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        //WHEN
        boolean recurrent = ticketDAO.isVehicleRecurrent("Car");
        //THEN
        assertFalse(recurrent);
    }

    @Test
    void isTheCarAlreadyParked() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis()-60*60*1000);
        ticket.setVehicleRegNumber("Car");
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        //WHEN
        boolean parked = ticketDAO.isVehicleAlreadyParked(ticket.getVehicleRegNumber());
        //THEN
        assertEquals("Car",ticketDAO.getTicket("Car").getVehicleRegNumber());
        assertThat(ticketDAO.getTicket(ticket.getVehicleRegNumber()).getOutTime()).isNull();
        assertTrue(parked);
    }

    @Test
    void isTheBikeAlreadyParked() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis()-60*60*1000);
        ticket.setVehicleRegNumber("Bike");
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(4,ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        //WHEN
        boolean parked = ticketDAO.isVehicleAlreadyParked(ticket.getVehicleRegNumber());
        //THEN
        assertEquals("Bike",ticketDAO.getTicket("Bike").getVehicleRegNumber());
        assertThat(ticketDAO.getTicket(ticket.getVehicleRegNumber()).getInTime()).isNotNull();
        assertTrue(parked);
    }

    @Test
    void isTheCarNotParkedYet() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis()-60*60*1000);
        ticket.setVehicleRegNumber("Car");
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,false);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        //WHEN
        boolean carNoParked = ticketDAO.isVehicleAlreadyParked("Car1");
        //THEN
        assertNotEquals("Car1",ticketDAO.getTicket("Car").getVehicleRegNumber());
        //Pour simplifier ce test, on peut tester directement cette méthode avec un null
        assertFalse(carNoParked);
    }

    @Test
    void isTheBikeNotParkedYet() {
        //GIVEN
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis()-60*60*1000);
        ticket.setVehicleRegNumber("Bike");
        ticket.setInTime(inTime);
        ParkingSpot parkingSpot = new ParkingSpot(4,ParkingType.BIKE,false);
        ticket.setParkingSpot(parkingSpot);
        ticketDAO.saveTicket(ticket);
        //WHEN
        boolean bikeNoParked = ticketDAO.isVehicleAlreadyParked("Bike1");
        //THEN
        assertNotEquals("Bike1",ticketDAO.getTicket("Bike").getVehicleRegNumber());
        assertFalse(bikeNoParked);
    }

}