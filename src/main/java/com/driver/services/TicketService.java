package com.driver.services;


import com.driver.EntryDto.BookTicketEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.PassengerRepository;
import com.driver.repository.TicketRepository;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    PassengerRepository passengerRepository;


    public Integer bookTicket(BookTicketEntryDto bookTicketEntryDto)throws Exception{

        //Check for validity
        //Use bookedTickets List from the TrainRepository to get bookings done against that train
        // Incase the there are insufficient tickets
        // throw new Exception("Less tickets are availabl
        //otherwise book the ticket, calculate the price and other details
        //Save the information in corresponding DB Tables
        //Fare System : Check problem statement
        //Incase the train doesn't pass through the requested stations
        //throw new Exception("Invalid stations");
        //Save the bookedTickets in the train Object
        //Also in the passenger Entity change the attribute bookedTickets by using the attribute bookingPersonId.
       //And the end return the ticketId that has come from db
    	
    	Optional<Train> train = trainRepository.findById(bookTicketEntryDto.getTrainId());
    	
    	if(!train.get().getRoute().contains(bookTicketEntryDto.getFromStation().toString()) || !train.get().getRoute().contains(bookTicketEntryDto.getToStation().toString())) {
    		throw new Exception("Invalid stations");
    	}
    	if(train.get().getNoOfSeats()<bookTicketEntryDto.getNoOfSeats()) {
    		throw new Exception("Less tickets are available");
    	}
    	
    	Ticket ticket = new Ticket();
    	
    	ticket.setPassengersList(passengerRepository.findAllById(bookTicketEntryDto.getPassengerIds()));
    	ticket.setTrain(train.get());
    	ticket.setFromStation(bookTicketEntryDto.getFromStation());
    	ticket.setToStation(bookTicketEntryDto.getToStation());
    	
    	int fare = 0;
    	String arr[]= train.get().getRoute().split(",");
    	int s=-1;
    	int r =-1;
		for(int i=0;i<arr.length;i++) {
			if(s==-1) {
				if(arr[i]==bookTicketEntryDto.getFromStation().toString()) {
					s=i;
				}
					
			}
			else {
				if(arr[i]==bookTicketEntryDto.getToStation().toString()) {
					r=i;
					break;
				}
			}
			
		
		}
		fare = (r-s)*300*bookTicketEntryDto.getNoOfSeats();
    	ticket.setTotalFare(fare);
    	
    	Ticket saved = ticketRepository.save(ticket);
    	train.get().setNoOfSeats(train.get().getNoOfSeats()-bookTicketEntryDto.getNoOfSeats());
    	trainRepository.save(train.get());
       return saved.getTicketId();

    }
}
