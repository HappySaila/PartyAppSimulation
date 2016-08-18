package animationSkeletonCode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;


import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

//model is separate from the view.
class outOfGridBoundsException extends Exception {
	   public outOfGridBoundsException(String msg){
	      super(msg);
	   }
	}
public class PartyApp {
//shared variables
	static int noPeople=8;
	static int totalPeople;

   	static int frameX=400;
	static int frameY=500;
	static int yLimit=400;
	static int gridX=10;
	static int gridY=10;

	static Person[] persons;
	static PersonMover[] personMover;
	
	static boolean done;  
	static PeopleCounter score = new PeopleCounter();

	static RoomPanel w;
	static RoomGrid grid;
	static CounterDisplay counterD ;

	public static AtomicBoolean pause = new AtomicBoolean(false);
	public static int roomLimit;

	
	
	public static void setupGUI(int frameX,int frameY,int [][] exits) {
		// Frame initialize and dimensions
    		JFrame frame = new JFrame("Party Animation"); 
    		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		frame.setSize(frameX, frameY);
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setSize(frameX,frameY);
 	    
		w = new RoomPanel(persons, grid, exits);
		w.setSize(frameX,frameY);
	    g.add(w);
	    
	    
	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
	    //NB fix below
	    JLabel caught =new JLabel("Inside: " + score.getInside() + "    ");
	    JLabel missed =new JLabel("Waiting:" + score.getWaiting()+ "    ");
	    JLabel scr =new JLabel("Left:" + score.getLeft()+ "    ");    
	    txt.add(caught);
	    txt.add(missed);
	    txt.add(scr);
	    g.add(txt);
	    //NB fix below
	    counterD = new CounterDisplay(caught, missed,scr,score);      //thread to update score
       
	    //Add start, pause and exit buttons
	    JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
        JButton startB = new JButton("Start");;
        
			// add the listener to the jbutton to handle the "pressed" event
			startB.addActionListener(new ActionListener() {
				boolean started=false;
		      public void actionPerformed(ActionEvent e)  {
		    	  	if (!started) {
		    	  		started=true;
			    	  	w.done=false;
			    	  	PersonMover.done=false;
					done =false;
	
			    		createThreads();
						pause.set(false); //game is now active and running
						//start threads one at a time
			      	for (int i=0;i<noPeople;i++) {
			      		personMover[i].start();
			      	}
		    	  	}
		    	  	}
		    });
			
			final JButton pauseB = new JButton("Pause ");;
			
			// add the listener to the jbutton to handle the "pressed" event
			pauseB.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  	//TODO fill in code here to create
		    	  // pause/resume button that works
				  synchronized (pause){
					  if (pause.get()==false){
						  //not paused and we want to pause the game
						  pause.set(true);
						  //game is now paused, change button label
						  pauseB.setText("Resume");
					  }else if(pause.get()==true){
						  //the game is paused and we want to unpause
						  pause.set(false);
						  //game is now resumed
						  pauseB.setText("Pause");
						  pause.notifyAll();
					  }
				  }
		      }
		    });
			
		JButton endB = new JButton("Quit");
				// add the listener to the jbutton to handle the "pressed" event
				endB.addActionListener(new ActionListener() {
			      public void actionPerformed(ActionEvent e) {
			    	  	// ask threads to stop
			    	  	PersonMover.done = true; //stop people moving
			    	  	counterD.done=true; //stop counter thread 
			    	  	w.repaint();
			    	  	w.done=true; //stop animator thread
			    	  	System.exit(0);
			      }
			    });

		b.add(startB);
		b.add(pauseB);
		b.add(endB);
		
		g.add(b);
    	
      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);     
       	//frame.pack();  // don't do this - packs it into small space
        frame.setVisible(true);

		
	}
	public static void createThreads() {
		score.resetScore();
 		//create people threads       
        for (int i=0;i<noPeople;i++) {
    			personMover[i] = new PersonMover(persons[i],score);
    		}
      	Thread t = new Thread(w); 
      	t.start();
      	//Start counter thread - for updating counters
      	Thread s = new Thread(counterD);  
      	s.start();
	}
	

	public static void main(String[] args) {
		if (args.length<3) {
			System.out.println("No command-line arguments supplied: <nopeople> <X> <Y>");
			System.exit(-1);
		}
		//deal with command line arguments
		noPeople=Integer.parseInt(args[0]);  //total people to enter room
		gridX=Integer.parseInt(args[1]); // No. of X grid cells  
		gridY=Integer.parseInt(args[2]); // No. of Y grid cells  
		roomLimit=Integer.parseInt(args[3]);
		//hardcoded exits
		int [][] exits = {{0,(int) gridY/2-1},
							{0,(int) gridY/2},  //two-cell wide door on left
							{gridX-1,(int) gridY/2-1},
							{gridX-1,(int) gridY/2}};	 //two-cell wide door on right
		
		grid = new RoomGrid(gridX, gridY,exits); //setup grid blocks	    
	    PersonMover.grid =grid; //grid shared with class
	    
		persons = new Person[noPeople];  //shared array of current words
		personMover = new PersonMover[noPeople];
		
		setupGUI(frameX, frameY,exits);  //Start GamePanel thread - for drawing animation
		
		for (int i=0;i<noPeople;i++) {
			persons[i]=new Person();
		}
		Scanner sc = new Scanner(System.in);
		while (true){
			sc.nextLine();
			System.out.println("getting...");
			System.out.println(grid.getEntranceBlock().getStatus());
			System.out.println("done...");
		}

	}

}
