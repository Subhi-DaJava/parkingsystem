package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.commons.math3.util.Precision;



public class FareCalculatorService {

    private TicketDAO ticketDAO;

    public void setTicketDAO(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    public FareCalculatorService() {
        this.ticketDAO = new TicketDAO();
    }

    public void calculateFare(Ticket ticket){


        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inMinute = ticket.getInTime().getTime();
        long outMinute =ticket.getOutTime().getTime();
        double fareByMinute;
        double  duration = (double) (outMinute - inMinute) / (1000 * 60 * 60);

        // Premières 30 minutes sont gratuites
        if( duration <= 0.5 ) {
            duration = 0;
        }

        double discountRate = 1;
        if(ticketDAO.isVehicleRecurrent(ticket.getVehicleRegNumber())){
            discountRate = 0.95;
        }

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                fareByMinute = Fare.CAR_RATE_PER_MINUTE;
                break;
            }
            case BIKE: {
                fareByMinute = Fare.BIKE_RATE_PER_MINUTE;
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }

        ticket.setPrice(duration * discountRate * fareByMinute);
    }

}