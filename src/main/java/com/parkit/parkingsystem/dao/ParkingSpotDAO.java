package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Tek
 * Contains two methods : one for getting the information (from) and another for updating the table parking in the prod DB
 */
public class ParkingSpotDAO {
    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");
    //Object for connecting the Database
    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * Checking and returning the number of parking for a vehicle type
     * @param parkingType
     * @return number of slot
     * @return  0 when parking is full
     */
    public int getNextAvailableSlot(ParkingType parkingType){
        Connection con = null;
        PreparedStatement ps =null;
        ResultSet rs = null;
        int result=-1;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();
            if(rs.next()){
                result = rs.getInt(1);
            }
        }catch (Exception ex){
            logger.error("Error fetching next available slot", ex);
        }finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closeConnection(con);
        }
        return result;
    }
    /**
     *
     * @param parkingSpot
     * @return true(get updated) or false
     */
    public boolean updateParking(ParkingSpot parkingSpot) {
        //update the availability for that parking slot
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();
            dataBaseConfig.closePreparedStatement(ps);
            return (updateRowCount == 1);
        } catch (Exception ex) {
            logger.error("Error updating parking info", ex);
            return false;
        } finally {
            if (ps != null) {
                dataBaseConfig.closePreparedStatement(ps);
            }
            dataBaseConfig.closeConnection(con);
        }
    }
}
