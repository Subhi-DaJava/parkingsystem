package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

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
    private void setUpPerTest() {
        dataBasePrepareService.clearDataBaseEntries();
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }

    @AfterAll
    private static void tearDown(){
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar(){

        parkingService.processIncomingVehicle();

        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        assertNull(ticket.getOutTime());
        assertNotNull(ticket.getInTime());
        assertEquals(2,parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
        assertEquals(0.0, ticket.getPrice());
        assertEquals(1,ticket.getParkingSpot().getId());
        assertTrue(ticketDAO.isVehicleAlreadyParked(ticket.getVehicleRegNumber()));
        assertEquals(ticket.getVehicleRegNumber(),ticketDAO.getTicket("ABCDEF").getVehicleRegNumber());
    }

    @Test
    public void testParkingLotExit() throws InterruptedException {
        ticketDAO.isVehicleAlreadyParked("ABCDEF");
        parkingService.processIncomingVehicle();
        Thread.sleep(1000);
        parkingService.processExitingVehicle();

        assertEquals(ticketDAO.getTicket("ABCDEF").getPrice(),0.0);
        assertNotNull(ticketDAO.getTicket("ABCDEF"));
        assertNotNull(ticketDAO.getTicket("ABCDEF").getOutTime());
    }

}
