package animationSkeletonCode;

import javax.swing.JLabel;

//no need to change any code here

public class CounterDisplay  implements Runnable {
	private PeopleCounter score;
	JLabel waiting;
	JLabel inside;
	JLabel left;
	public static  volatile boolean done=false;
	
	CounterDisplay(JLabel w, JLabel i, JLabel l, PeopleCounter score) {
        this.waiting=w;
        inside = i;
        left = l;
        this.score=score;
    }
	
	public void run() {
        while (!done) {
            try {
                //Thread sleeps & updates 
                waiting.setText("Inside: " + score.getInside() + "    ");
                inside.setText("Waiting:" +  score.getWaiting()+ "    " );
                left.setText("Left:" + score.getLeft()+ "    " );  //setText is thread safe (I think)
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
    }
}
