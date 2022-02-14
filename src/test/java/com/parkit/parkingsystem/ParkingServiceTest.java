package com.parkit.parkingsystem;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;
    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @BeforeEach
    private void setUpPerTest() {
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processExitingVehicleTest() throws Exception {
        //GIVEN
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE,false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        when(ticketDAO.isVehicleAlreadyParked("ABCDEF")).thenReturn(true);
        //WHEN
        parkingService.processExitingVehicle();
        //THEN
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO,times(1)).getTicket(anyString());
        verify(ticketDAO).isVehicleAlreadyParked(anyString());
        assertEquals(1, Precision.round(ticketDAO.getTicket("ABCDEF").getPrice(),2));
        assertTrue(parkingSpot.isAvailable());
    }

    @Test
    public void processExistingVehicleNotParkedTest() throws Exception {
        //GIVEN
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.isVehicleAlreadyParked("ABCDEF")).thenReturn(false);
        //WHEN
        parkingService.processExitingVehicle();
        //THEN
        assertNull(ticketDAO.getTicket("ABCDEF"));
    }

    @Test
    public void processExitingVehicleCouldNotUpdateTicketTest() throws Exception {
        //GIVEN
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.isVehicleAlreadyParked("ABCDEF")).thenReturn(true);
        when(ticketDAO.updateTicket(ticket)).thenReturn(false);
        //WHEN
        parkingService.processExitingVehicle();
        //THEN
        verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO,times(1)).getTicket(anyString());
        verify(ticketDAO,times(1)).updateTicket(any(Ticket.class));
        verify(ticketDAO).isVehicleAlreadyParked(anyString());
        assertNotEquals(0, Precision.round(ticketDAO.getTicket("ABCDEF").getPrice(),2));

    }

    @Test
    public void processExitingVehicleCouldNotBeProcessedTest() throws Exception {
        //GIVEN
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.isVehicleAlreadyParked("ABCDEF")).thenReturn(true);
        when(ticketDAO.updateTicket(ticket)).thenReturn(false);
        //WHEN
        parkingService.processExitingVehicle();
        //THEN
        verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO,times(1)).getTicket(anyString());
        verify(ticketDAO,times(1)).updateTicket(any(Ticket.class));
        verify(ticketDAO).isVehicleAlreadyParked(anyString());
        assertNotEquals(0, Precision.round(ticketDAO.getTicket("ABCDEF").getPrice(),2));

    }

    @Test
    public void processIncomingVehicleWithoutDiscountTest() throws Exception {
        //GIVEN
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(ticketDAO.isVehicleAlreadyParked("ABCDEF")).thenReturn(false);
        when(ticketDAO.isVehicleRecurrent("ABCDEF")).thenReturn(false);
        //WHEN
        parkingService.processIncomingVehicle();
        //THEN
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO,times(1)).saveTicket(any(Ticket.class));
        verify(parkingSpotDAO).getNextAvailableSlot(any(ParkingType.class));
        verify(ticketDAO, never()).getTicket("ABCDEF");
        verify(ticketDAO).isVehicleAlreadyParked(anyString());
    }

    @Test
    public void processIncomingVehicleWithDiscountTest() throws Exception {
        //GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(4);
        when(ticketDAO.isVehicleRecurrent("ABCDEF")).thenReturn(true);
        when(ticketDAO.isVehicleAlreadyParked("ABCDEF")).thenReturn(false);
        //WHEN
        parkingService.processIncomingVehicle();
        //THEN
        verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO,times(1)).saveTicket(any(Ticket.class));
        verify(parkingSpotDAO).getNextAvailableSlot(ParkingType.BIKE);
        verify(ticketDAO).isVehicleAlreadyParked(anyString());
    }

    @Test
    public void processIncomingVehicleAlreadyParkedTest() throws Exception {
        //GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(ticketDAO.isVehicleAlreadyParked("ABCDEF")).thenReturn(true);
        //WHEN
        parkingService.processIncomingVehicle();
        //THEN
        verify(parkingSpotDAO, times(0)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO,times(0)).saveTicket(any(Ticket.class));
        verify(parkingSpotDAO).getNextAvailableSlot(any(ParkingType.class));
        verify(ticketDAO, never()).getTicket("ABCDEF");
        verify(ticketDAO).isVehicleAlreadyParked(anyString());
    }

    @Test
    public void processIncomingVehicleNoPlaceInTheParkingTest() {
        //GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);
        ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
        //THEN
        assertNull(parkingSpot);
    }

    @Test
    public  void getNextParkingNumberIfAvailableTest(){
        //GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        //WHEN
        int parkingSpotNumber = parkingService.getNextParkingNumberIfAvailable().getId();
        //THEN
        assertEquals(1, parkingSpotNumber);
    }

    @Test
    public  void wrongVehicleTypeThrowExceptionTest(){
        //GIVEN
        when(inputReaderUtil.readSelection()).thenReturn(3);
        //THEN
        assertThrows(IllegalArgumentException.class, ()-> parkingService.getVehicleType());
    }




}
