package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InteractiveShellTest {

    private InteractiveShell interactiveShell;
    @Mock
    ParkingService parkingService;
    @Mock
    InputReaderUtil inputReaderUtil;

    @BeforeEach
    public void setUp(){
        interactiveShell = new InteractiveShell();
        interactiveShell.setInputReaderUtil(inputReaderUtil);
        interactiveShell.setParkingService(parkingService);
    }
    @AfterAll
    private static void tearDown(){

    }

    @Test
    void loadInterfaceForExistingSystemTest(){

        when(inputReaderUtil.readSelection()).thenReturn(3);

        interactiveShell.loadInterface();

        verify(inputReaderUtil).readSelection();

    }

    @Test
    void loadInterfaceForIncomingVehicle() {
        when(inputReaderUtil.readSelection()).thenReturn(1,3);

        interactiveShell.loadInterface();

        verify(inputReaderUtil,times(2)).readSelection();
        verify(parkingService, times(1)).processIncomingVehicle();
    }

    @Test
    void loadInterfaceForExistingVehicle() {
        when(inputReaderUtil.readSelection()).thenReturn(2,3);

        interactiveShell.loadInterface();

        verify(inputReaderUtil,times(2)).readSelection();
        verify(parkingService,times(1)).processExitingVehicle();
    }

    @Test
    void loadInterfaceWrongEnter() {
        when(inputReaderUtil.readSelection()).thenReturn(4,3);

        interactiveShell.loadInterface();

        verify(inputReaderUtil, times(2)).readSelection();

    }

}