package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;


public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double inMinute = (double) ticket.getInTime().getTime() / (1000*60 );
        double outMinute = (double) ticket.getOutTime().getTime() / (1000*60 );

        double  duration = outMinute - inMinute;
        double discount = 1;

        TicketDAO ticketDAO = new TicketDAO();
        boolean isVehicleRecurring = ticketDAO.checkByVehicleRegNumber(ticket.getVehicleRegNumber());

        double parkingFee = discount * duration;
        if (isVehicleRecurring)
            parkingFee *= (discount-0.05);

        // Premières 30 minutes sont gratuites
        if( duration <= 30 )
            ticket.setPrice(0.0);
         else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(parkingFee * Fare.CAR_RATE_PER_MINUTE );
                     break;
                }
                case BIKE: {
                    ticket.setPrice(parkingFee * Fare.BIKE_RATE_PER_MINUTE );
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }

        }

    }

}