//M. M. Kuttel 2016

package animationSkeletonCode;

public class RoomGrid {
	private GridBlock [][] Blocks;
	private final int x;
	private final int y;
	
	
	RoomGrid() {
		this.x=20;
		this.y=20;
		Blocks = new GridBlock[x][y];
		int [] [] dfltExit= {{10,10}};
		this.initGrid(dfltExit);
	}
	
	RoomGrid(int x, int y, int [][] exitBlocks) {
		this.x=x;
		this.y=y;
		Blocks = new GridBlock[x][y];
		this.initGrid(exitBlocks);
	}
	
	private void initGrid(int [][] exitBlocks) {
		for (int i=0;i<x;i++) {
			for (int j=0;j<y;j++) {
				boolean exit=false;
				boolean refreshment=false;
				for (int e=0;e<exitBlocks.length;e++)
						if ((i==exitBlocks[e][0])&&(j==exitBlocks[e][1])) 
							exit=true;
				if (j==(y-1)) refreshment=true; //bar is at the end of the room
				Blocks[i][j]=new GridBlock(i,j,exit,refreshment);
				
			}
		}
	}
	
	public void printGrid() {
		for (int j=0;j<y;j++) {
			for (int i=0;i<x;i++) 	{
				System.out.print(" " + Blocks[i][j].getStatus());
			}
			System.out.println();
		}
	}
	
	public int getMaxX() {
		return x;
	}
	
	public int getMaxY() {
		return y;
	}
	
	public int [] getEntrance() { //hard coded entrance
		int [] coords = new int [] {getMaxX()/2,0};
		return coords;
	}

	public boolean inGrid(int i, int j) {
		if ((i>=x) || (j>=y) ||(i<0) || (j<0)) 
			return false;
		return true;
		
	}
	public GridBlock getEntranceBlock() {
		return Blocks[getMaxX()/2][0];
	}
	
	public GridBlock getBlock(int i, int j) {  //return block associated with grid position
		if (!inGrid(i,j)) 
			return null;
		return Blocks[i][j];
	}
	
}
 



	

	

