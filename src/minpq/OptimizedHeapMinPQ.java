package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class OptimizedHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of item-priority pairs.
     */
    private final List<PriorityNode<T>> items; //A list of PriorityNodes
    /**
     * {@link Map} of each item to its associated index in the {@code items} heap.
     */
    private final Map<T, Integer> itemToIndex; //HashMap to store items and modify
    /**
     * The number of elements in the heap.
     */
    private int size;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        items = new ArrayList<>();
        itemToIndex = new HashMap<>();
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        // add as leaf
        this.items.add(new PriorityNode<>(item,priority));
        this.size++;
        int ind = 1;
        if (this.size > 1) {
            ind = swim(this.size);
        }
        this.itemToIndex.put(item, ind);
    }

    @Override
    public boolean contains(T item) {
        return (this.itemToIndex.get(item) != null);
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return this.items.get(1).item();
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode minNode = this.items.get(1);
        // swap with right most leaf
        swap(1, this.size);
        // delete smallest (now leaf)
        this.itemToIndex.remove(minNode.item());
        this.items.remove(this.items.get(this.size));
        this.size--;
        // sink root
        sink(1);
        return (T) minNode.item();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        int i = this.itemToIndex.get(item);
        // swap item and last item
        swap(i,this.size);
        // remove last item
        this.items.remove(this.items.get(this.size));
        size--;
        // sink lowest priority item
        sink(i);
        // add item with new priority
        PriorityNode n = new PriorityNode<>(item,priority);
        this.items.add(n);
        size++;
        // update index
        this.itemToIndex.put(item,this.items.indexOf(item));
    }

    @Override
    public int size() {
        return this.size;
    }

    // Swim the item at the given index until the heap invariant is satisfied.
    private int swim(int index) {
        int parentIndex=0;
        while (index > 1) {
            parentIndex = index / 2;

            // If the item at the parent index is greater, swap the two items.
            if (isGreater(parentIndex, index)) {
                swap(parentIndex, index);
            } else {
                return index;
            }
            index = parentIndex;
        }
        return index;
    }

    // Sink the item at the given index until the heap invariant is satisfied.
    private void sink(int index) {
        while (2 * index <= this.size) {
            int leftIndex = 2 * index;
            int rightIndex = leftIndex + 1;

            // Assign the smaller of the children as the swap candidate.
            int swapCandidate = leftIndex;
            if (rightIndex <= size && isGreater(leftIndex, rightIndex)) {
                swapCandidate = rightIndex;
            }

            // If the item at the current index is greater, swap the two items.
            if (isGreater(index, swapCandidate)) {
                swap(index, swapCandidate);
            } else {
                return;
            }
            index = swapCandidate;
        }
    }

    // Returns true if and only if the item at index i is strictly greater
    // than the item at index j. i or j must be valid indices.
    private boolean isGreater(int i, int j) {
        boolean isValidI = 0 < i && i <= size;
        boolean isValidJ = 0 < j && j <= size;
        if (isValidI && isValidJ) {
            // If both i and j are valid, return whether i is greater than j.
            return Double.compare(this.items.get(i).priority(),this.items.get(j).priority())> 0;
        } else if (isValidI || isValidJ) {
            // Only one of i or j is valid, so return whether i is valid.
            // If i is valid, then it is greater than j.
            // If i is not valid, then it is less than j.
            return isValidI;
        } else {
            throw new IllegalArgumentException("i or j must be a valid index");
        }
    }

    // Swaps the items at the given indices i and j.
    private void swap(int i, int j) {
        PriorityNode<T> tempi = this.items.get(i);
        PriorityNode<T> tempj = this.items.get(j);
        this.items.set(i, this.items.get(j));
        this.itemToIndex.put(tempi.item(),j);
        this.items.set(j, tempi);
        this.itemToIndex.put(tempj.item(),i);
    }
}
