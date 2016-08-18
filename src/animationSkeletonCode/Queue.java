package animationSkeletonCode;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by wlsgra012 on 2016/08/17.
 */
public class Queue {
    //will create a concurrent blocking queue to make sure the people who arrive at the door
    //enter in that order
    private BlockingQueue<PersonMover> queue = new ArrayBlockingQueue<PersonMover>(10);
}
