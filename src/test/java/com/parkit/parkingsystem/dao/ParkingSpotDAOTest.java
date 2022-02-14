package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tester les deux méthodes de la classe ParkingSpotDAO
 */
class ParkingSpotDAOTest {

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
    void getNextAvailableSlotForCar() {
        //GIVEN
        ParkingType parkingType =ParkingType.CAR;
        //WHEN
        int parkingSpotNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
        //THEN
        assertEquals(1,parkingSpotNumber);

    }

    @Test
    void getNextAvailableSlotForBike() {
        //GIVEN
        ParkingType parkingType =ParkingType.BIKE;
        //WHEN
        int parkingSpotNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
        //THEN
        assertEquals(4,parkingSpotNumber);

    }

    @Test
    void getNextAvailableSlotForUnknownType() {
        //GIVEN
        ParkingType parkingType =ParkingType.UNKNOWN;
        //WHEN
        int parkingSpotNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
        //THEN
        assertNotEquals(1,parkingSpotNumber);
    }

    @Test
    void getNextAvailableSlotNotDisponibleForCar() {
        //GIVEN
        ParkingType parkingType = ParkingType.CAR;
        ParkingSpot parkingSpot = new ParkingSpot(1,parkingType,false);
        parkingSpotDAO.updateParking(parkingSpot);
        //WHEN
        int parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
        //THEN
        assertNotEquals(1,parkingNumber);
    }
    @Test
    void getNextAvailableSlotNotDisponibleForBike() {
        //GIVEN
        ParkingType parkingType = ParkingType.BIKE;
        ParkingSpot parkingSpot = new ParkingSpot(4,parkingType,false);
        parkingSpotDAO.updateParking(parkingSpot);
        //WHEN
        int parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
        //THEN
        assertNotEquals(4,parkingNumber);

    }

    @Test
    void updateParkingForCar() {
        //GIVEN
        ParkingType parkingType =ParkingType.CAR;
        ParkingSpot parkingSpot;
        int number = parkingSpotDAO.getNextAvailableSlot(parkingType);
        parkingSpot = new ParkingSpot(3,parkingType,false);
        //WHEN
        parkingSpotDAO.updateParking(parkingSpot);
        //THEN
        assertEquals(3,parkingSpot.getId());
        assertEquals(1,number);
    }
    @Test
    void updateParkingForBike() {
        //GIVEN
        ParkingType parkingType =ParkingType.BIKE;
        ParkingSpot parkingSpot = new ParkingSpot(5,parkingType,false);
        int number = parkingSpotDAO.getNextAvailableSlot(parkingType);
        //WHEN
        parkingSpotDAO.updateParking(parkingSpot);
        //THEN
        assertEquals(5,parkingSpot.getId());
        assertEquals(4,number);
    }

}