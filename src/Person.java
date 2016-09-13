
import java.awt.Color;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Person extends Thread{

	GridBlock currentBlock;

	public int getMovingSpeed() {
		return movingSpeed;
	}

	private int movingSpeed;
	private static int maxWait=10;
	private static int minWait=0;
	private int X;
	private int Y;
	private boolean freed;
	
	private static Random rand = new Random();
	
	private Color myColor;
	private boolean inRoom;
	private boolean thirsty;

	public static AtomicBoolean pause = new AtomicBoolean(false);




	Person() {
		movingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
		float c = rand.nextFloat();
		myColor = new Color(c, rand.nextFloat(), c);
		X=-1;
		Y=-1;
		inRoom=false;
		thirsty=true;
	}
	
//characters are shared by a mover thread and GamePanel (for visualization)
	
	public synchronized void initBlock(GridBlock b) {
		currentBlock=b;
		X=currentBlock.getX();
		Y=currentBlock.getY();
		inRoom=true;
	}
	
	public  boolean moveToBlock(GridBlock newBlock)  {
		//System.out.println("Attempting transfer...from x,y"+X+","+Y +"to x,y"+newBlock.getX()+","+newBlock.getY()); //debug
		synchronized (pause){
			while (pause.get()==true){
				try {
					pause.wait();
					freed = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (freed){
				try {
					sleep(rand.nextInt(100));
					freed=false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(newBlock.getBlock()!=false) {
				currentBlock.releaseBlock();
				initBlock(newBlock);
				//System.out.println("Transfering... to x,y"+newBlock.getX()+","+newBlock.getY()); //debug
				return true;
			}
			else  {
				//System.out.println("Transfer to x,y"+newBlock.getX()+","+newBlock.getY()); //debug
				return false;
			}
		}
	}

	
	public   boolean atExit() { return currentBlock.isExit();}	
	public  boolean atRefreshmentStation() { 
		return currentBlock.isRefreshmentStation();}	
	public boolean inRoom() {
		return inRoom;
	}
	public boolean thirsty() {
		return thirsty;
	}
	
	public  void leave() {
		X=-1;
		Y=-1;
		currentBlock.releaseBlock();
		inRoom=false;
	}
	
	public void drink() {
		thirsty=false;
	}
	
	public  int getX() { return X;}	
	
	public  int getY() {	return Y;	}
	
	public int getSpeed() {
		return movingSpeed;
	}

	
	public Color getColor() {
		return myColor;
	}

	public  void setColor(Color c) {
		myColor=c;
	}


	}
	
	
	


