package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceTest {

    @Mock
    private static TicketDAO ticketDAO;

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;


    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCarWith35MinutesParkingTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 35 * 60 * 1000 ) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertEquals( (double) 35/60 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWith35MinutesParkingTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 35 * 60 * 1000 ) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertThat( (double) 35/60 * Fare.BIKE_RATE_PER_HOUR).isEqualTo(ticket.getPrice());
    }
    @Test
    public void calculateFareUnknownType(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.UNKNOWN,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //THEN
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }
    @Test
    public void calculateFareUnknownTypeWithFutureInTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.UNKNOWN,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareParkingTypeNull(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //THEN
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareParkingTypeNullWithFutureInTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //WHEN
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareParkingTypeNullPakringSpotNumber0(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        ParkingSpot parkingSpot = new ParkingSpot(0, null,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);
        //THEN
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //THEN
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareCarWithFutureInTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        //THEN
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareCarWithFutureInTimeAndNullOutTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);
        //THEN
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTimeAndNullOutTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);
        //THEN
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWith45MinutesParkingTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) ); //45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertThat(0.75 * Fare.BIKE_RATE_PER_HOUR).isEqualTo(ticket.getPrice());
    }

    @Test
    public void calculateFareCarWith45MinutesParkingTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) ); //45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertEquals( 0.75 * Fare.CAR_RATE_PER_HOUR,ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithADayParkingTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) ); //24 hours parking time should give 24 *60 parking fare per minute
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertEquals( 24 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }
    @Test
    public void calculateFareCarWithLessThan30MinutesParkingTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) ); //30 minutes parking time should give 0.0 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertEquals( 0.0,ticket.getPrice());
    }
    @Test
    public void calculateFareCarWithLessThan15MinutesParkingTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  15 * 60 * 1000) ); //15 minutes parking time should give 0.0 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertEquals( 0.0, ticket.getPrice());
    }
    @Test
    public void calculateFareBikeWithLessThan30MinutesParkingTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) ); //30 minutes parking time should give 0.0 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertEquals( 0.0, ticket.getPrice());
    }
    @Test
    public void calculateFareBikeWithLessThan15MinutesParkingTime(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  15 * 60 * 1000) ); //15 minutes parking time should give 0.0 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertEquals( 0.0, ticket.getPrice());
    }

    @Test
    public void verifyVehicleIsRecurring() {
        //GIVEN
        when(ticketDAO.isVehicleRecurrent(anyString())).thenReturn(true);
        //WHEN
        ticketDAO.isVehicleRecurrent("abcde");
        //THEN
        verify(ticketDAO,times(1)).isVehicleRecurrent(anyString());
    }

    @Test
    public void calculateFarCarWithDiscount() {
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("abcde");
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(true);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertEquals( Fare.CAR_RATE_PER_HOUR * 0.95, ticket.getPrice());
        verify(ticketDAO, Mockito.times(1)).isVehicleRecurrent(ticket.getVehicleRegNumber());

    }

    @Test
    public void calculateFarBikeWithDiscount() {
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setVehicleRegNumber("abcde");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(true);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertEquals(Fare.BIKE_RATE_PER_HOUR * 0.95, ticket.getPrice());
        verify(ticketDAO, Mockito.times(1)).isVehicleRecurrent(ticket.getVehicleRegNumber());

    }

    @Test
    public void calculateFarBikeWithDiscountLessThan30Minute(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 30 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setVehicleRegNumber("abcde");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(true);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertEquals(0, ticket.getPrice());
        verify(ticketDAO, Mockito.times(1)).isVehicleRecurrent(ticket.getVehicleRegNumber());

    }
    @Test
    public void calculateFarCarWithDiscountLessThan30Minute(){
        //GIVEN
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 30 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setVehicleRegNumber("abcde");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(true);
        fareCalculatorService.setTicketDAO(ticketDAO);
        //WHEN
        fareCalculatorService.calculateFare(ticket);
        //THEN
        assertEquals(0, ticket.getPrice());
        verify(ticketDAO, Mockito.times(1)).isVehicleRecurrent(ticket.getVehicleRegNumber());

    }
}
