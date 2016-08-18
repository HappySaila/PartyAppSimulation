package animationSkeletonCode;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by wlsgra012 on 2016/08/17.
 */
public class Queue {
    //will create a concurrent blocking queue to make sure the people who arrive at the door
    //enter in that order
    private BlockingQueue<PersonMover> queue;
    public Queue(int guests){
        queue = new ArrayBlockingQueue<PersonMover>(guests);
    }

    public void arrived(PersonMover person) throws InterruptedException{
        queue.put(person);
    }

    public PersonMover enter() throws InterruptedException{
        return queue.take();
    }
}
