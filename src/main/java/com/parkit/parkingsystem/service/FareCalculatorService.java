package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

/**
 * Calculate the fare for a vehicle (less than 30 minutes and a recurring vehicle)
 * @author Tek and Subhi
 *
 */
public class FareCalculatorService {

    private TicketDAO ticketDAO;
    public void setTicketDAO(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }
    public FareCalculatorService() {
        this.ticketDAO = new TicketDAO();
    }

    /**
     * When a ticket is providing(when a vehicle exit), this methode calculate the parking fee.
     * @param ticket
     * @Throws IllegalArgumentException for Unknown type of vehicle
     */
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour =ticket.getOutTime().getTime();
        double fareByHour;
        double  duration = (double) (outHour - inHour) / (1000 * 60 * 60);

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
                fareByHour = Fare.CAR_RATE_PER_HOUR;
                break;
            }
            case BIKE: {
                fareByHour = Fare.BIKE_RATE_PER_HOUR;
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }

        ticket.setPrice(duration * discountRate * fareByHour);
    }

}