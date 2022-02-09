package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InteractiveShellTest {
    /*private InteractiveShell interactiveShell;*/
    private ParkingService parkingService;

    @Mock
    InputReaderUtil inputReaderUtil;
    @Mock
    ParkingSpotDAO parkingSpotDAO;
    @Mock
    TicketDAO ticketDAO;


    @BeforeEach
    public void setUp(){
        parkingService = new ParkingService(inputReaderUtil,parkingSpotDAO,ticketDAO);
        InteractiveShell.setInputReaderUtil(inputReaderUtil);
    }

    @Test
    void loadInterfaceForExistingSystemTest() throws Exception {

        when(inputReaderUtil.readSelection()).thenReturn(3);
        InteractiveShell.loadInterface();

        verify(inputReaderUtil).readSelection();
        verify(ticketDAO,never()).getTicket(any());
        verify(parkingSpotDAO,times(0)).getNextAvailableSlot(any());
    }

    @Test
    @Disabled
    void loadInterfaceForIncomingVehicle() throws Exception {

        when(inputReaderUtil.readSelection()).thenReturn(1);
        InteractiveShell.setInputReaderUtil(inputReaderUtil);
        ParkingType parkingType = parkingService.getVehicleType();
        //InteractiveShell.loadInterface();
        when(inputReaderUtil.readSelection()).thenReturn(3);
        InteractiveShell.loadInterface();

        verify(inputReaderUtil,times(2)).readSelection();
    }

    @Test
    @Disabled
    void loadInterfaceForExistingVehicle() throws Exception {

        when(inputReaderUtil.readSelection()).thenReturn(2);

        when(ticketDAO.isVehicleAlreadyParked(anyString())).thenReturn(true);

        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        //when(ticketDAO.updateTicket(any())).thenReturn(false);
        //when(inputReaderUtil.readSelection()).thenReturn(3);

        InteractiveShell.loadInterface();

        verify(inputReaderUtil,times(1)).readSelection();
    }


    @Test
    @Disabled
    void loadInterfaceWrongEnter() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(4);

        InteractiveShell.loadInterface();

        verify(inputReaderUtil, times(1)).readSelection();
        verify(ticketDAO,never()).getTicket(any());
        verify(parkingSpotDAO,times(0)).getNextAvailableSlot(any());
    }




}