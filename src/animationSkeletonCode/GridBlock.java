package animationSkeletonCode;

import java.util.concurrent.atomic.AtomicBoolean;

public class GridBlock {
	static private int IDcounter=0;
	private boolean occupied;
	private final boolean isExit;
	private final boolean isRefreshmentStation;
	private int ID;
	private int [] coords;
	
	GridBlock(boolean exitBlock, boolean refreshBlock) {
		occupied=false;
		isExit=exitBlock;
		isRefreshmentStation=refreshBlock;
		synchronized (GridBlock.class) {
			ID=IDcounter;
			GridBlock.IDcounter++;
			}
	}
	GridBlock(int x, int y, boolean exitBlock, boolean refreshBlock) {
		this(exitBlock,refreshBlock);
		coords = new int [] {x,y};
	}
	
	public int getX() {return coords[0];}
	public int getY() {return coords[1];}

	
	public void waitBlock()  {
		occupied=true;
	}
	
	
	public boolean getBlock() {
		if (occupied==true) {
			return false;
		}
		occupied=true;
		return true;
	}
		
	public void releaseBlock() {
		occupied=false;
	}
	
	public boolean getStatus() {
		return occupied;	
	}
	
	public boolean isExit() {
		return isExit;	
	}
	
	public boolean isRefreshmentStation() {
		return isRefreshmentStation;
	}
	public int getID() { return ID;}
	
}
