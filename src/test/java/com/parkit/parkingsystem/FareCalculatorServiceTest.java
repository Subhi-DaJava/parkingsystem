package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Date;

@Tag("FareCalculationTest")
@DisplayName("Calculer les fees de Car et Bike.")
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
    @Tag("FeeCar")
    public void calculateFareCarWith35MinutesParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 35 * 60 * 1000 ) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);

        fareCalculatorService.calculateFare(ticket);

        assertEquals( (double) 35/60 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    @Tag("FeeBike")
    public void calculateFareBikeWith35MinutesParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 35 * 60 * 1000 ) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);

        fareCalculatorService.calculateFare(ticket);

        assertThat( (double) 35/60 * Fare.BIKE_RATE_PER_HOUR).isEqualTo(ticket.getPrice());
    }
    @Test
    @Tag("FeeForUnknownType")
    public void calculateFareUnknownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.UNKNOWN,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }
    @Test
    @Tag("FeeForUnknownType")
    public void calculateFareUnknownTypeWithFutureInTime(){
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
    @Tag("FeeForUnknownType")
    public void calculateFareParkingTypeNull(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @Tag("FeeForUnknownType")
    public void calculateFareParkingTypeNullWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @Tag("FeeForUnknownType")
    public void calculateFareParkingTypeNullPakringSpotNumber0(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );

        ParkingSpot parkingSpot = new ParkingSpot(0, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @Tag("FeeBike")
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @Tag("FeeCar")
    public void calculateFareCarWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @Tag("FeeCar")
    public void calculateFareCarWithFutureInTimeAndNullOutTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @Tag("FeeBike")
    public void calculateFareBikeWithFutureInTimeAndNullOutTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @Tag("FeeBike")
    public void calculateFareBikeWith45MinutesParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) ); //45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);

        fareCalculatorService.calculateFare(ticket);

        assertThat(0.75 * Fare.BIKE_RATE_PER_HOUR).isEqualTo(ticket.getPrice());
    }

    @Test
    @Tag("FeeCar")
    public void calculateFareCarWith45MinutesParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) ); //45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);
        fareCalculatorService.calculateFare(ticket);

        assertEquals( 0.75 * Fare.CAR_RATE_PER_HOUR,ticket.getPrice());
    }

    @Test
    @Tag("FeeCar")
    public void calculateFareCarWithADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) ); //24 hours parking time should give 24 *60 parking fare per minute
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);

        fareCalculatorService.calculateFare(ticket);

        assertEquals( 24 * Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }
    @Test
    @Tag("FeeCar")
    public void calculateFareCarWithLessThan30MinutesParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) ); //30 minutes parking time should give 0.0 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);

        fareCalculatorService.calculateFare(ticket);

        assertEquals( 0.0,ticket.getPrice());
    }
    @Test
    @Tag("FeeCar")
    public void calculateFareCarWithLessThan15MinutesParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  15 * 60 * 1000) ); //15 minutes parking time should give 0.0 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);

        fareCalculatorService.calculateFare(ticket);
        assertEquals( 0.0, ticket.getPrice());
    }
    @Test
    @Tag("FeeBike")
    public void calculateFareBikeWithLessThan30MinutesParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  30 * 60 * 1000) ); //30 minutes parking time should give 0.0 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);

        fareCalculatorService.calculateFare(ticket);
        assertEquals( 0.0, ticket.getPrice());
    }
    @Test
    @Tag("Bike")
    public void calculateFareBikeWithLessThan15MinutesParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  15 * 60 * 1000) ); //15 minutes parking time should give 0.0 parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        when(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())).thenReturn(false);
        fareCalculatorService.setTicketDAO(ticketDAO);

        fareCalculatorService.calculateFare(ticket);
        assertEquals( 0.0, ticket.getPrice());
    }

    //Test vérifier un véhicule est récurrent ou non
    @Test
    public void verifyVehicleIsRecurring() throws Exception {
        when(ticketDAO.isVehicleRecurrent(anyString())).thenReturn(true);
        ticketDAO.isVehicleRecurrent("abcde");

        verify(ticketDAO,times(1)).isVehicleRecurrent(anyString());
    }

    @Test
    public void calculateFarCarWithDiscount() throws Exception {
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

        fareCalculatorService.calculateFare(ticket);

        assertEquals( Fare.CAR_RATE_PER_HOUR * 0.95, ticket.getPrice());
        verify(ticketDAO, Mockito.times(1)).isVehicleRecurrent(ticket.getVehicleRegNumber());

    }

    @Test
    public void calculateFarBikeWithDiscount() throws Exception {
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

        fareCalculatorService.calculateFare(ticket);

        assertEquals(Fare.BIKE_RATE_PER_HOUR * 0.95, ticket.getPrice());
        verify(ticketDAO, Mockito.times(1)).isVehicleRecurrent(ticket.getVehicleRegNumber());

    }

    @Test
    public void calculateFarBikeWithDiscountLessThan30Minute() throws Exception {
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

        fareCalculatorService.calculateFare(ticket);

        assertEquals(0, ticket.getPrice());

        verify(ticketDAO, Mockito.times(1)).isVehicleRecurrent(ticket.getVehicleRegNumber());

    }
    @Test
    public void calculateFarCarWithDiscountLessThan30Minute() throws Exception {
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

        fareCalculatorService.calculateFare(ticket);

        assertEquals(0, ticket.getPrice());
        verify(ticketDAO, Mockito.times(1)).isVehicleRecurrent(ticket.getVehicleRegNumber());

    }






}
