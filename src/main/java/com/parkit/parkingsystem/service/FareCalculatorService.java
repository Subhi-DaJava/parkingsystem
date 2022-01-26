package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.commons.math3.util.Precision;



public class FareCalculatorService {


    public void calculateFare(Ticket ticket){

        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        TicketDAO ticketDAO = new TicketDAO();
        double inMinute = ticket.getInTime().getTime() / (1000 * 60 );
        double outMinute =ticket.getOutTime().getTime() / (1000 * 60 );

        double  duration = outMinute - inMinute;
        double discountRate = 1.0;
        boolean isVehicleRecurring = ticketDAO.checkByVehicleRegNumber(ticket.getVehicleRegNumber());

        double parkingFee = discountRate * duration;
        if (isVehicleRecurring) {
            parkingFee = parkingFee * (discountRate - 0.05);
        }

        // Premières 30 minutes sont gratuites
        if( duration <= 30 )
            ticket.setPrice(0.0);
        else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(Precision.round(parkingFee * Fare.CAR_RATE_PER_MINUTE,2));
                     break;
                }
                case BIKE: {
                    ticket.setPrice(Precision.round(parkingFee * Fare.BIKE_RATE_PER_MINUTE,2));
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }

        }

    }

}