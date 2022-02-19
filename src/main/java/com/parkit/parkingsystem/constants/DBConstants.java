package com.parkit.parkingsystem.constants;

public class DBConstants {
    /**
     * @Author Tek and Subhi
     * Provide all SQL queries for searching, setting, and updating in the DB information
     */
    //For getting the number of slot for a vehicle type from parking table
    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
    //For updating the parking table, his slot number and his availability
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";
    //For saving a ticket in the table ticket
    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
    //For updating the ticket
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    //For getting the ticket
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME DESC limit 1";
    //For checking a vehicle is parked once or not (registered in the ticket table)
    public static final String VEHICLE_RECURRENT = "select VEHICLE_REG_NUMBER from ticket where VEHICLE_REG_NUMBER = ? and PRICE > 0";
    //For checking a vehicle is parking now or not in the parking ( registered in the parking table )
    public static final String CHECK_VEHICLE_PARKING = "select VEHICLE_REG_NUMBER from ticket where VEHICLE_REG_NUMBER = ? and isnull(OUT_TIME)";
}
