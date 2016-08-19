
public class PeopleCounter {
	private int peopleOutSide; //counter for people arrived but not yet in the building
	private int peopleInside;
	private int peopleLeft;
	
	PeopleCounter() {
		peopleOutSide=0;
		peopleInside=0;
		peopleLeft=0;
	}
		
	// all getters and setters must be synchronized
	
	synchronized public int getWaiting() {
		return peopleOutSide;
	}

	synchronized public int getInside() {
		return peopleInside;
	}
	
	synchronized public int getTotal() {
		return (peopleOutSide+peopleInside+peopleLeft);
	}

	synchronized public int getLeft() {
		return peopleLeft;
	}
	
	synchronized public void personArrived() {
		peopleOutSide++;
	}
	synchronized public void personEntered() {
		peopleOutSide--;
		peopleInside++;
	}

	synchronized public void personLeft() {
		peopleInside--;
		peopleLeft++;
		
	}

	synchronized public void resetScore() {
		peopleInside=0;
		peopleOutSide=0;
		peopleLeft=0;
	}
}
