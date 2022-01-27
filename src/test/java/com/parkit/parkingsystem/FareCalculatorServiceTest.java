package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 31 * 60 * 1000) );
        Date outTime = new Date();

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( Precision.round(31 * Fare.CAR_RATE_PER_MINUTE,2), ticket.getPrice());
    }

    @Test
    @Tag("FeeBike")
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 31 * 60 * 1000 ) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( Precision.round(31 * Fare.BIKE_RATE_PER_MINUTE,2), ticket.getPrice());
    }

    @Test
    @Tag("FeeUnknown")
    public void calculateFareUnknownType(){
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
    @Tag("FeeBike")
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) ); //45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(Precision.round(45 * Fare.BIKE_RATE_PER_MINUTE,2), ticket.getPrice());
    }

    @Test
    @Tag("FeeCar")
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) ); //45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( Precision.round(45 * Fare.CAR_RATE_PER_MINUTE, 2),ticket.getPrice());
    }

    @Test
    @Tag("FeeCar")
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) ); //24 hours parking time should give 24 *60 parking fare per minute
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( Precision.round(24*60 * Fare.CAR_RATE_PER_MINUTE, 2), ticket.getPrice());
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
        fareCalculatorService.calculateFare(ticket);
        assertEquals( 0.0, ticket.getPrice());
    }

    //un test pour remise en mock la class ticket

    //vérify the véhicule récurrent or not

    @Test
    public void verifyVehicleIsRecurring() throws Exception {

        when(ticketDAO.checkByVehicleRegNumber(any())).thenReturn(true);
        //doReturn(true).when(ticketDAO).checkByVehicleRegNumber(anyString());
        assertTrue(ticketDAO.checkByVehicleRegNumber("abcde"));
        verify(ticketDAO,times(1)).checkByVehicleRegNumber(anyString());
    }
    @Test
    public void calculateFarCarRemise() throws Exception {



        //when(ticketDAO.getTicket(vehicleRegNumber)).thenReturn(ticket);
        //doReturn(true).when(ticketDAO).checkByVehicleRegNumber(anyString());
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - ( 60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setVehicleRegNumber("abcde");
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        when(ticketDAO.checkByVehicleRegNumber(ticket.getVehicleRegNumber())).thenReturn(true);

        fareCalculatorService.calculateFare(ticket);

        assertEquals(Precision.round(60 * Fare.CAR_RATE_PER_MINUTE * 0.95,2), ticket.getPrice());
        verify(ticketDAO,times(1)).checkByVehicleRegNumber(anyString());
        //verify(ticketDAO).checkByVehicleRegNumber(vehicleRegNumber);
    }


}
