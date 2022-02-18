package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.*;
import java.util.Date;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketDAOTest {
    private TicketDAO ticketDAO;
    Ticket ticket;
    Date inTime;
    @Mock
    private static DataBaseTestConfig dataBaseTestConfig;
    @Mock
    private static PreparedStatement ps;
    @Mock
    private static ResultSet rs;
    @Mock
    private static Connection con;

    @BeforeEach
    void setUp() throws SQLException, IOException, ClassNotFoundException {
        ticketDAO = new TicketDAO();
        ticket = new Ticket();
        inTime = new Date();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        when(dataBaseTestConfig.getConnection()).thenReturn(con);

    }

    @Test
    public void saveTicketForCarParking() throws SQLException {
        ticket.setId(1);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(1,ParkingType.CAR,false));
        ticket.setPrice(0.0);
        ticket.setOutTime(null);
        ticket.setInTime(new Date(System.currentTimeMillis()));

        when(con.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(ps);
        doNothing().when(ps).setInt(1,ticket.getParkingSpot().getId());
        doNothing().when(ps).setString(2,ticket.getVehicleRegNumber());
        doNothing().when(ps).setDouble(3,ticket.getPrice());
        doNothing().when(ps).setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
        doNothing().when(ps).setTimestamp(5,null);
        when(ps.execute()).thenReturn(true);

        assertTrue(ticketDAO.saveTicket(ticket));
    }
    @Test
    public void saveTicketForBikeForOneHourParking() throws SQLException {
        ticket.setId(1);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(4,ParkingType.BIKE,true));
        ticket.setPrice(1);
        ticket.setInTime(new Date(System.currentTimeMillis()-60*60*1000));
        ticket.setOutTime(new Date(System.currentTimeMillis()));

        when(con.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(ps);
        doNothing().when(ps).setInt(1,ticket.getParkingSpot().getId());
        doNothing().when(ps).setString(2,ticket.getVehicleRegNumber());
        doNothing().when(ps).setDouble(3,ticket.getPrice());
        doNothing().when(ps).setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
        doNothing().when(ps).setTimestamp(5,new Timestamp(ticket.getOutTime().getTime()));
        when(ps.execute()).thenReturn(true);

        assertTrue(ticketDAO.saveTicket(ticket));
    }
    @Test
    public void saveTicketFailureForCarEnteringInTheParking() throws SQLException {
        ticket.setId(1);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(0,ParkingType.CAR,true));
        ticket.setPrice(1.5);
        ticket.setOutTime(null);
        ticket.setInTime(new Date(System.currentTimeMillis()));

        when(con.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(ps);
        doNothing().when(ps).setInt(1,ticket.getParkingSpot().getId());
        doNothing().when(ps).setString(2,ticket.getVehicleRegNumber());
        doNothing().when(ps).setDouble(3,ticket.getPrice());
        doNothing().when(ps).setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
        doNothing().when(ps).setTimestamp(5,null);
        //when(ps.execute()).thenReturn(false);

        assertFalse(ticketDAO.saveTicket(ticket));
    }
    @Test
    public void saveTicketFailure_ForCar_whenExitingParking() throws SQLException {
        ticket.setId(1);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(1,ParkingType.CAR,true));
        ticket.setPrice(1.5);
        ticket.setInTime(new Date(System.currentTimeMillis()));
        ticket.setOutTime(new Date(System.currentTimeMillis()-60*1000*60));

        when(con.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(ps);
        doNothing().when(ps).setInt(1,ticket.getParkingSpot().getId());
        doNothing().when(ps).setString(2,ticket.getVehicleRegNumber());
        doNothing().when(ps).setDouble(3,ticket.getPrice());
        doNothing().when(ps).setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
        doNothing().when(ps).setTimestamp(5,new Timestamp(ticket.getOutTime().getTime()));
        //when(ps.execute()).thenReturn(true);

        assertFalse(ticketDAO.saveTicket(ticket));
    }
    @Test
    public void saveTicketFailureForCarEnteringInTheParking_whenDB_Error() throws SQLException {
        ticket.setId(1);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(1,ParkingType.CAR,false));
        ticket.setPrice(0.0);
        ticket.setOutTime(null);
        ticket.setInTime(new Date(System.currentTimeMillis()));

        when(con.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(ps);
        doNothing().when(ps).setInt(1,ticket.getParkingSpot().getId());
        doNothing().when(ps).setString(2,ticket.getVehicleRegNumber());
        doNothing().when(ps).setDouble(3,ticket.getPrice());
        doNothing().when(ps).setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
        doNothing().when(ps).setTimestamp(5,null);
        when(ps.execute()).thenThrow(SQLException.class);

        assertFalse(ticketDAO.saveTicket(ticket));
    }
    @Test
    public void saveTicketFailure_whenVehicleRegNumberIsNull() throws SQLException {
        ticket.setId(1);
        ticket.setVehicleRegNumber("");
        ticket.setParkingSpot(new ParkingSpot(1,ParkingType.CAR,true));
        ticket.setPrice(1.5);
        ticket.setOutTime(null);
        ticket.setInTime(new Date(System.currentTimeMillis()));

        when(con.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(ps);
        doNothing().when(ps).setInt(1,ticket.getParkingSpot().getId());
        doNothing().when(ps).setString(2,ticket.getVehicleRegNumber());
        doNothing().when(ps).setDouble(3,ticket.getPrice());
        doNothing().when(ps).setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
        doNothing().when(ps).setTimestamp(5,null);
        when(ps.execute()).thenReturn(false);

        assertFalse(ticketDAO.saveTicket(ticket));
    }

    @Test
    public void getTicketForCarForOneHourParking() throws SQLException {
        ticket.setId(1);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(1,ParkingType.CAR,true));
        ticket.setPrice(1.5);
        ticket.setInTime(new Date(System.currentTimeMillis()-60*60*1000));
        ticket.setOutTime(new Date(System.currentTimeMillis()));

        when(con.prepareStatement(DBConstants.GET_TICKET)).thenReturn(ps);
        doNothing().when(ps).setString(1,ticket.getVehicleRegNumber());
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(ticket.getParkingSpot().getId());
        when(rs.getInt(2)).thenReturn(ticket.getId());
        when(rs.getDouble(3)).thenReturn(ticket.getPrice());
        when(rs.getTimestamp(4)).thenReturn(new Timestamp(ticket.getInTime().getTime()));
        when(rs.getTimestamp(5)).thenReturn(new Timestamp(ticket.getOutTime().getTime()));
        when(rs.getString(6)).thenReturn(ParkingType.CAR.toString());

        Ticket ticketNew = ticketDAO.getTicket(ticket.getVehicleRegNumber());

        assertEquals(ticketNew.getVehicleRegNumber(),ticket.getVehicleRegNumber());
        assertEquals(ticketNew.getInTime(),ticket.getInTime());
        assertEquals(ticketNew.getOutTime(),ticket.getOutTime());
    }

    @Test
    public void getTicketForBikeFor30MinutesParking() throws SQLException {
        ticket.setId(4);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(4,ParkingType.CAR,false));
        ticket.setPrice(0.0);
        ticket.setInTime(new Date(System.currentTimeMillis()-30*60*1000));
        ticket.setOutTime(null);
        Date outTime= new Date();
        outTime.setTime(System.currentTimeMillis());
        ticket.setOutTime(outTime);

        when(con.prepareStatement(DBConstants.GET_TICKET)).thenReturn(ps);
        doNothing().when(ps).setString(1,ticket.getVehicleRegNumber());
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(ticket.getParkingSpot().getId());
        when(rs.getInt(2)).thenReturn(ticket.getId());
        when(rs.getDouble(3)).thenReturn(ticket.getPrice());
        when(rs.getTimestamp(4)).thenReturn(new Timestamp(ticket.getInTime().getTime()));
        when(rs.getTimestamp(5)).thenReturn(new Timestamp(ticket.getOutTime().getTime()));
        when(rs.getString(6)).thenReturn(ParkingType.CAR.toString());

        Ticket ticketNew = ticketDAO.getTicket(ticket.getVehicleRegNumber());

        assertEquals(ticketNew.getVehicleRegNumber(),ticket.getVehicleRegNumber());
        assertEquals(ticketNew.getInTime(),ticket.getInTime());
        assertEquals(ticketNew.getOutTime(),ticket.getOutTime());
    }
    @Test
    public void doesNotGetTicket() throws SQLException {
        ticket.setId(4);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(4,ParkingType.CAR,false));
        ticket.setPrice(0.0);
        ticket.setInTime(new Date(System.currentTimeMillis()-60*60*1000));
        ticket.setOutTime(null);
        Date outTime= new Date();
        outTime.setTime(System.currentTimeMillis());
        ticket.setOutTime(outTime);

        when(con.prepareStatement(DBConstants.GET_TICKET)).thenReturn(ps);
        doNothing().when(ps).setString(1,ticket.getVehicleRegNumber());
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        Ticket ticketNew = ticketDAO.getTicket(ticket.getVehicleRegNumber());

        assertNull(ticketNew);
    }
    @Test
    public void getTicketForUnknown() throws SQLException {
        ticket.setId(1);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(1,ParkingType.UNKNOWN,false));
        ticket.setPrice(2);
        ticket.setInTime(new Date(System.currentTimeMillis()-60*60*1000));
        ticket.setOutTime(null);

        when(con.prepareStatement(DBConstants.GET_TICKET)).thenThrow(SQLException.class);
        Ticket ticketNew = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        assertNull(ticketNew);
    }

    @Test
    public void getTicket_whenDB_Error() throws SQLException {
        ticket.setId(1);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(0,ParkingType.CAR,false));
        ticket.setPrice(2);
        ticket.setInTime(new Date(System.currentTimeMillis()-60*60*1000));
        ticket.setOutTime(null);

        when(con.prepareStatement(DBConstants.GET_TICKET)).thenThrow(SQLException.class);

        Ticket ticketNew = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        assertNull(ticketNew);
    }

    @Test
    void updateTicketForCar() throws SQLException {
        //GIVEN

        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Car");
        ticket.setInTime(inTime);
        ticket.setParkingSpot(new ParkingSpot(1,ParkingType.CAR,false));
        ticket.setId(1);
        ticket.setPrice(1.5);

        when(con.prepareStatement(DBConstants.UPDATE_TICKET)).thenReturn(ps);
        Date outTime = new Date();
        outTime.setTime(System.currentTimeMillis());
        ticket.setOutTime(outTime);
        doNothing().when(ps).setDouble(1,ticket.getPrice());
        doNothing().when(ps).setTimestamp(2,new Timestamp(ticket.getOutTime().getTime()));
        doNothing().when(ps).setInt(3, ticket.getId());
        //WHEN
        boolean updated = ticketDAO.updateTicket(ticket);
        //THEN
        assertTrue(updated);
        assertEquals(1.5,ticket.getPrice());
    }
    @Test
    void updateTicketForBike() throws SQLException {
        //GIVEN

        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Bike");
        ticket.setInTime(inTime);
        ticket.setParkingSpot(new ParkingSpot(4,ParkingType.BIKE,false));
        ticket.setId(4);
        ticket.setPrice(1);

        when(con.prepareStatement(DBConstants.UPDATE_TICKET)).thenReturn(ps);
        Date outTime = new Date();
        outTime.setTime(System.currentTimeMillis());
        ticket.setOutTime(outTime);
        doNothing().when(ps).setDouble(1,ticket.getPrice());
        doNothing().when(ps).setTimestamp(2,new Timestamp(ticket.getOutTime().getTime()));
        doNothing().when(ps).setInt(3, ticket.getId());
        //WHEN
        boolean updated = ticketDAO.updateTicket(ticket);
        //THEN
        assertTrue(updated);
        assertEquals(1,ticket.getPrice());
    }
    @Test
    void updateTicketForUnknownType() throws SQLException {
        //GIVEN
        inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
        ticket.setVehicleRegNumber("Unknown");
        ticket.setInTime(inTime);
        ticket.setParkingSpot(new ParkingSpot(1,ParkingType.UNKNOWN,false));
        ticket.setId(1);
        ticket.setPrice(1);
        when(con.prepareStatement(DBConstants.UPDATE_TICKET)).thenThrow(SQLException.class);
        Date outTime = new Date();
        outTime.setTime(System.currentTimeMillis());
        ticket.setOutTime(outTime);
        //WHEN
        boolean updated = ticketDAO.updateTicket(ticket);
        //THEN
        assertFalse(updated);
    }

    @Test
    void isVehicleCarRecurrent() throws SQLException {

        ticket.setId(1);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(1,ParkingType.CAR,true));
        ticket.setPrice(1.5);
        ticket.setInTime(new Date(System.currentTimeMillis()-60*60*1000));
        ticket.setOutTime(new Date(System.currentTimeMillis()));
        when(con.prepareStatement(DBConstants.VEHICLE_RECURRENT)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        doNothing().when(ps).setString(1,ticket.getVehicleRegNumber());
        when(rs.next()).thenReturn(true);

        boolean recurrent = ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber());

        assertTrue(recurrent);
    }
    @Test
    void isVehicleBikeRecurrent() throws SQLException {
        ticket.setId(4);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(4,ParkingType.BIKE,true));
        ticket.setPrice(1);
        ticket.setInTime(new Date(System.currentTimeMillis()-60*60*1000));
        ticket.setOutTime(new Date(System.currentTimeMillis()));
        when(con.prepareStatement(DBConstants.VEHICLE_RECURRENT)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        doNothing().when(ps).setString(1,ticket.getVehicleRegNumber());
        when(rs.next()).thenReturn(true);

        boolean recurrent = ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber());

        assertTrue(recurrent);
    }
    @Test
    void isVehicleNoRecurrent() throws SQLException {
        ticket.setId(4);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(4,ParkingType.BIKE,true));
        ticket.setPrice(1);
        ticket.setInTime(new Date(System.currentTimeMillis()-60*60*1000));
        ticket.setOutTime(new Date(System.currentTimeMillis()));
        when(con.prepareStatement(DBConstants.VEHICLE_RECURRENT)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        doNothing().when(ps).setString(1,ticket.getVehicleRegNumber());
        when(rs.next()).thenReturn(false);

        boolean recurrent = ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber());

        assertFalse(recurrent);
    }
    @Test
    void throwsNullWhenDBError() throws SQLException {
        ticket.setId(4);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(4, ParkingType.BIKE, true));
        ticket.setPrice(1);
        ticket.setInTime(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        ticket.setOutTime(new Date(System.currentTimeMillis()));
        when(con.prepareStatement(DBConstants.VEHICLE_RECURRENT)).thenThrow(SQLException.class);

        boolean recurrent = ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber());

        assertFalse(recurrent);
    }

        @Test
    public void isTheCarAlreadyParked() throws SQLException {
            ticket.setId(1);
            ticket.setVehicleRegNumber("ABCDEF");
            ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
            ticket.setPrice(0.0);
            ticket.setInTime(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
            ticket.setOutTime(null);

            when(con.prepareStatement(DBConstants.CHECK_VEHICLE_PARKING)).thenReturn(ps);
            doNothing().when(ps).setString(1,ticket.getVehicleRegNumber());
            when(ps.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(true);

            boolean parked = ticketDAO.isVehicleAlreadyParked(ticket.getVehicleRegNumber());

            assertTrue(parked);
    }
    @Test
    public void isTheBikeAlreadyParked() throws SQLException {
        ticket.setId(4);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, false));
        ticket.setPrice(0.0);
        ticket.setInTime(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        ticket.setOutTime(null);

        when(con.prepareStatement(DBConstants.CHECK_VEHICLE_PARKING)).thenReturn(ps);
        doNothing().when(ps).setString(1,ticket.getVehicleRegNumber());
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        boolean parked = ticketDAO.isVehicleAlreadyParked(ticket.getVehicleRegNumber());

        assertTrue(parked);
    }
    @Test
    public void isTheVehicleNotParkedYet() throws SQLException {
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setOutTime(null);
        when(con.prepareStatement(DBConstants.CHECK_VEHICLE_PARKING)).thenReturn(ps);
        doNothing().when(ps).setString(1,ticket.getVehicleRegNumber());
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        boolean parked = ticketDAO.isVehicleAlreadyParked(ticket.getVehicleRegNumber());

        assertFalse(parked);
    }
    @Test
    public void whenVehicleRegNumberIsNull() throws SQLException {

        ticket.setOutTime(null);
        when(con.prepareStatement(DBConstants.CHECK_VEHICLE_PARKING)).thenReturn(ps);
        doNothing().when(ps).setString(1,null);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        boolean parked = ticketDAO.isVehicleAlreadyParked(ticket.getVehicleRegNumber());

        assertFalse(parked);
    }
    @Test
    public void isTheVehicleThrowsNullWhenDB_Error() throws SQLException {

        when(con.prepareStatement(DBConstants.CHECK_VEHICLE_PARKING)).thenThrow(SQLException.class);

        boolean parked = ticketDAO.isVehicleAlreadyParked(ticket.getVehicleRegNumber());

        assertFalse(parked);
    }


}