package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InteractiveShellTest {

    @Mock
    InputReaderUtil inputReaderUtil;

    @Mock
    ParkingSpotDAO parkingSpotDAO;
    @Mock
    TicketDAO ticketDAO;


    @Test
    @Disabled
    void loadInterface() throws Exception {
        InteractiveShell.loadInterface();

    }
}