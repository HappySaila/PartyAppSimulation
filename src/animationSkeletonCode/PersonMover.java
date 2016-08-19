package animationSkeletonCode;

import java.util.Random;
import java.util.concurrent.*;


public class PersonMover extends Thread {
	public static int IDcounter=0;
	private Person thisPerson;
	public static RoomGrid grid; //shared grid
	private Random rand;
	private PeopleCounter counter;

	public volatile static boolean done=false; //add stop button
	
	private int ID; //thread ID for debugging

	//my variables

	PersonMover( Person janeDoe) {
		thisPerson = janeDoe;
		rand = new Random();	

		synchronized (PersonMover.class) {
			ID=IDcounter;
			PersonMover.IDcounter++;
			}
	}
	
	PersonMover( Person creature, PeopleCounter score) {
		this(creature);
		this.counter=score;
	}
	
	
	
	private void headForRefreshments() {  //next move towards bar
		//no need to change this function
		int x_mv=-1;
		int y_mv=-1;
		GridBlock nextBlock =null;
		while (nextBlock==null) { // repeat until hit on empty cell
			
			x_mv= rand.nextInt(3)-1+thisPerson.getX();
			if (x_mv<0) x_mv=0;
			
			y_mv=thisPerson.getY()+1;
			if (y_mv<0) y_mv=0;		
			else if (y_mv>=grid.getMaxY()) y_mv=grid.getMaxY()-1;	
			
			if (!((x_mv==thisPerson.getX())&&(y_mv==thisPerson.getY()))) {
				nextBlock=grid.getBlock(x_mv,y_mv);
			} 
		}	
		thisPerson.moveToBlock(nextBlock);
	}
	
	private void mingle() { //random next move
		//no need to change this function
		int x_mv=-1;
		int y_mv=-1;
		GridBlock nextBlock =null;
		while (nextBlock==null) { // repeat until hit on empty cell
			x_mv= rand.nextInt(3)-1+thisPerson.getX();
			if (x_mv<0) x_mv=0;
			y_mv=rand.nextInt(3)-1+thisPerson.getY();;
			if (y_mv<0) y_mv=0;		
			if (!((x_mv==thisPerson.getX())&&(y_mv==thisPerson.getY()))) {
					//System.out.println("moving from x="+x+" y="+y); //debug
					//System.out.println("moving to x="+x_mv+" y="+y_mv); //debug
				nextBlock=grid.getBlock(x_mv,y_mv);
			} 
		}	
		thisPerson.moveToBlock(nextBlock);
		
	}
	
	public void run() {
		//TODO - add code so threads wait until other threads are ready before starting
		//TODO add code to allow for pausing
		
		try {
			sleep(rand.nextInt(10000)); //time till arriving at party
			//arrived at the party, so add to queue
			PartyApp.queue.arrived(this);
			//to make sure that people cant enter the party if the simulation is paused
			synchronized (Person.pause){
				while(Person.pause.get()==true){
					Person.pause.wait();
				}
			}

			counter.personArrived(); //add to counter

			GridBlock firstBlock =grid.getEntranceBlock(); //enter through entrance
			assert(firstBlock!=null);

			synchronized (GridBlock.entranceLock){
				while(firstBlock.getStatus()==true || (counter.getInside()>=PartyApp.roomLimit)){
					GridBlock.entranceLock.wait();
				}
				//pop person from the queue
				PartyApp.queue.enter();
				firstBlock.waitBlock(); //threads should wait until first block is free
			}

			thisPerson.initBlock(firstBlock);
			counter.personEntered(); //add to counter
			sleep(thisPerson.getSpeed());
		} catch (InterruptedException e1) {
			done=true;
		}

		while ((!done)&&(thisPerson.inRoom())) {
			//must lock all people here when the game is paused
			try {
				if (thisPerson.thirsty()) {
					if (thisPerson.atRefreshmentStation()) {
						sleep(thisPerson.getSpeed()*4);//drinking for a while
						thisPerson.drink();
					}
					else headForRefreshments();
				}
				else if (thisPerson.atExit()) {
					thisPerson.leave();
				}
				else {
					mingle();
				}
				sleep(thisPerson.getSpeed());
			}

			catch (InterruptedException e) {
				System.out.println("Thread "+this.ID + "interrrupted.");
				done=true;
			}
		}
		counter.personLeft(); //add to counter
	
	}
	
}
