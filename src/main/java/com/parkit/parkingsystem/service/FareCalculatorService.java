package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
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
        double discount = 0.95;

        TicketDAO ticketDAO = new TicketDAO();
        boolean vehicleRecurring = ticketDAO.checkByVehicleRegNumber(ticket.getVehicleRegNumber());

        // Premières 30 minutes sont gratuites
        if( duration <= 30 )
            ticket.setPrice(0.0);
         else {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    if(vehicleRecurring) {
                        ticket.setPrice(duration * Fare.CAR_RATE_PER_MINUTE * discount);
                        System.out.println("You benefit from a 5% discount on the normal fee.\n");
                    }
                    else {
                        ticket.setPrice(duration * Fare.CAR_RATE_PER_MINUTE);
                    }
                    break;
                }
                case BIKE: {
                    if(vehicleRecurring) {
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_MINUTE * discount);
                        System.out.println("You benefit from a 5% discount on the normal fee.\n");
                    }else {
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_MINUTE);
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }



    }

}