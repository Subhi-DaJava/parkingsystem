package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {
    private ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static DataBaseTestConfig dataBaseTestConfig;
    @Mock
    private static PreparedStatement ps;
    @Mock
    private static ResultSet rs;
    @Mock
    private static Connection con;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException, IOException {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        when(dataBaseTestConfig.getConnection()).thenReturn(con);
    }

    @Test
    void getNextAvailableSlotForCar() throws SQLException {
        //GIVEN
        when(con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        doNothing().when(ps).setString(1, String.valueOf(ParkingType.CAR));
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(2);

        //WHEN
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        //THEN
        assertEquals(2,result);
    }
    @Test
    void getNextAvailableSlotForBike() throws SQLException {
        //GIVEN
        when(con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        doNothing().when(ps).setString(1, String.valueOf(ParkingType.BIKE));
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(4);

        //WHEN
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);

        //THEN
        assertEquals(4, result);
    }
    @Test
    void getNextAvailableSlotForUnknownType() throws SQLException {
        //GIVEN
        when(con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        doNothing().when(ps).setString(1, String.valueOf(ParkingType.UNKNOWN));
        //WHEN
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.UNKNOWN);
        //THEN
        assertEquals(-1, result);
    }
    @Test
    void getNextAvailableSlotNotDisponibleForCar() throws SQLException {
        //GIVEN
        when(con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        doNothing().when(ps).setString(1,String.valueOf(ParkingType.CAR));
        when(rs.next()).thenReturn(false);
        //WHEN
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        //THEN
        assertEquals(-1,result);
    }
    @Test
    void getNextAvailableSlotNotDisponibleForBike() throws SQLException {
        //GIVEN
        when(con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        doNothing().when(ps).setString(1,String.valueOf(ParkingType.BIKE));
        when(rs.next()).thenReturn(false);
        //WHEN
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
        //THEN
        assertEquals(-1,result);
    }
    @Test
    void getNextAvailableSlot_throwsSQLException_whenDBAccessError() throws SQLException {
        //GIVEN
        when(con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(ps);
        when(ps.executeQuery()).thenThrow(SQLException.class);
        //WHEN
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        //THEN
        assertEquals(-1,result);

    }
    @Test
    void updateParkingForCarIncoming() throws SQLException {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,false);
        when(con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(ps);
        /*doNothing().when(ps).setBoolean(1,false);
        doNothing().when(ps).setInt(2,1);*/
        when(ps.executeUpdate()).thenReturn(1);
        //THEN
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }
    @Test
    void updateParkingForCarExiting() throws SQLException {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(2,ParkingType.CAR,true);
        when(con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(ps);
        /*doNothing().when(ps).setBoolean(1,true);
        doNothing().when(ps).setInt(2,2);*/
        when(ps.executeUpdate()).thenReturn(1);
        //THEN
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }
    @Test
    void updateParkingForBikeIncoming() throws SQLException {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(5,ParkingType.BIKE,true);
        when(con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(ps);
   /*     doNothing().when(ps).setBoolean(1,true);
        doNothing().when(ps).setInt(2,5);*/
        when(ps.executeUpdate()).thenReturn(1);
        //THEN
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }
    @Test
    void updateParkingForBikeExiting() throws SQLException {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(4,ParkingType.BIKE,false);
        when(con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(ps);
        /*doNothing().when(ps).setBoolean(1,false);
        doNothing().when(ps).setInt(2,4);*/
        when(ps.executeUpdate()).thenReturn(1);
        //THEN
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }
    @Test
    void updateParkingForUnknownType() throws SQLException {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.UNKNOWN,false);
        when(con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(ps);
        doNothing().when(ps).setBoolean(1,false);
        doNothing().when(ps).setInt(2,1);
        when(ps.executeUpdate()).thenReturn(0);
        //THEN
        assertFalse(parkingSpotDAO.updateParking(parkingSpot));
    }
    @Test
    void updateParking_withInvalidSpotNumber() throws SQLException {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(0,ParkingType.CAR,false);
        when(con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(ps);
        doNothing().when(ps).setBoolean(1,false);
        doNothing().when(ps).setInt(2,0);
        when(ps.executeUpdate()).thenReturn(0);
        //THEN
        assertFalse(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    void updateParking_throwsSQLException_whenUpdateError() throws SQLException {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,false);
        when(con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenThrow(SQLException.class);
        //THEN
        assertFalse(parkingSpotDAO.updateParking(parkingSpot));
    }



}
