package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;


public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double inMinute = ticket.getInTime().getTime() / (1000*60 );

        double outMinute =ticket.getOutTime().getTime() / (1000*60 );

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double  duration = outMinute - inMinute;
        // Premières 30 minutes sont gratuites
        if( duration <= 30 )
            ticket.setPrice(0.0);
        else {
            switch (ticket.getParkingSpot().getParkingType()) {

                case CAR: {
                        ticket.setPrice(duration * Fare.CAR_RATE_PER_MINUTE);
                    break;
                }
                case BIKE: {
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_MINUTE);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
    }
}