//M. M Kuttel 2016

package animationSkeletonCode;
//no need to change anything here
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JPanel;

public class RoomPanel extends JPanel implements Runnable {
		public static volatile boolean done; //to signal when to stop
		private Person[] persons;
		private int noPeople;
		private int [][] exits;

		RoomGrid grid; //shared grid
		
		RoomPanel(Person[] chrctrs, 	 RoomGrid grid,int [][] exits) {
			this.persons=chrctrs; 
			noPeople = chrctrs.length;
			done=false;
			this.grid = grid;
			this.exits=exits;
		}
		
		public void paintComponent(Graphics g) {
		
			
		    int width = getWidth();
		    int height = getHeight();
		    int maxY = grid.getMaxY();
		    int maxX= grid.getMaxX();
		    int wInc= width/(maxX+2); //1 space on either side
		    int hInc= height/(maxY+2);//2 spaces on bottome
		    g.clearRect(0,0,width,height);
		    
		    g.setColor(Color.black);
		    
		    //draw grid lines
		    for (int i=1;i<=(maxX+1);i++)  //columns 
		    		g.drawLine(i*wInc, 0, i*wInc, maxY*hInc); //- leave space at bottom
		    for (int i=0;i<=maxY;i++) //rows 
		    		g.drawLine(wInc, i*hInc, (maxX+1)*wInc, i*hInc); //- leave space at sides
		    
		  //highlight the entrance
		    g.setColor(Color.gray);
		    int [] entrance = grid.getEntrance();
		    	g.fillRect(entrance[0]*wInc+wInc, entrance[1]*hInc, wInc, hInc);
		    
		    //highlight the exit blocks
		    g.setColor(Color.pink);
		    for (int e=0;e<exits.length;e++) {
		    		g.fillRect(exits[e][0]*wInc+wInc, exits[e][1]*hInc, wInc, hInc);
		    }
		    
		    		    
		    //draw and label bar
		    g.setColor(Color.lightGray);
		    g.fillRect(wInc, maxY*hInc, wInc*(maxX), hInc*3);
		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.BOLD, hInc));
		    g.drawString("Bar",(maxX-1)*wInc/2,maxY*hInc+hInc);	

		   //draw the ovals representing people
			int x,y;
		    for (int i=0;i<noPeople;i++){	    	
		    		if (persons[i].inRoom()) {
			    		g.setColor(persons[i].getColor());
			    		x= persons[i].getX()*wInc+wInc;
			    		y= persons[i].getY()*hInc;
			    		//System.out.println(i+" at "+x + " " +y);//debug
			    		g.fillOval(x, y , wInc, hInc);
		    		}
		    }
		   }
	
		public void run() {
			while (!done) {
				repaint();
				try {
					Thread.sleep(10); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				};
			}
		}

	}


