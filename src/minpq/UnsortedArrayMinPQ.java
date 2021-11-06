package minpq;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Unsorted array (or {@link ArrayList}) implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class UnsortedArrayMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the item-priority pairs in no specific order.
     */
    private final List<PriorityNode<T>> items;

    /**
     * Constructs an empty instance.
     */
    public UnsortedArrayMinPQ() {
        items = new ArrayList<>();
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        this.items.add(new PriorityNode<>(item, priority));
    }

    @Override
    public boolean contains(T item) {
        for (PriorityNode<T> i:this.items) {
            if (i.item().equals(item)) {
                return true;
            }
        }
        return false;
    }

    private PriorityNode<T> getMinNode(){
        PriorityNode<T> min = this.items.get(0);
        // look for min priority in items
        for (PriorityNode<T> i:this.items){
            if (i.priority() < min.priority()){
                min = i;
            }
        }
        return min;
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return (T) getMinNode().item();
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode<T> i = getMinNode();
        this.items.remove(i);
        return (T) i.item();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        // look for item
        for (PriorityNode<T> i: this.items) {
            if (i.item().equals(item)) {
                i.setPriority(priority);
                return;
            }
        }
    }

    @Override
    public int size() {
        int s = 0;
        for (PriorityNode i:this.items){
            s++;
        }
        return s;
    }
}
