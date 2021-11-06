package minpq;

import java.util.*;

/**
 * {@link TreeMap} and {@link HashMap} implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class DoubleMapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link NavigableMap} of priority values to all items that share the same priority values.
     */
    private final NavigableMap<Double, Set<T>> priorityToItem;
    /**
     * {@link Map} of items to their associated priority values.
     */
    private final Map<T, Double> itemToPriority;

    /**
     * Constructs an empty instance.
     */
    public DoubleMapMinPQ() {
        priorityToItem = new TreeMap<>();
        itemToPriority = new HashMap<>();
    }

    @Override
    public void add(T item, double priority) {
        // using the HashMap to see if the item is in the tree
        // O(1)
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }

        // create a new key-pair value <Priority,Set> because items can be in the same priority level
        // TreeMap is proficient in retrieving priority levels (ordered data structure)
        // O(log N) for both methods
        if (!priorityToItem.containsKey(priority)) {
            priorityToItem.put(priority, new HashSet<>());
        }
        // get the Set that contains the priority level and add the item into the set for searching purposes
        // O(log N)
        Set<T> itemsWithPriority = priorityToItem.get(priority);

        itemsWithPriority.add(item);

        //the HashMap helps to access the priority quickly of an item.
        // O(1) because the number of elements in the set is constant
        // in asymptotic analysis
        itemToPriority.put(item, priority);
    }

    @Override
    public boolean contains(T item) {
        return itemToPriority.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        double minPriority = priorityToItem.firstKey();
        Set<T> itemsWithMinPriority = priorityToItem.get(minPriority);
        return firstOf(itemsWithMinPriority);
    }

    @Override
    public T removeMin() {
        //O(1)
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        //O(1)
        double minPriority = priorityToItem.firstKey();
        //O (logN)
        Set<T> itemsWithMinPriority = priorityToItem.get(minPriority);
        //O(1)
        T item = firstOf(itemsWithMinPriority);
        //O(1)
        itemsWithMinPriority.remove(item);
        //O(1)
        if (itemsWithMinPriority.isEmpty()) {
            // O(logN)
            priorityToItem.remove(minPriority);
        }
        //O(1)
        itemToPriority.remove(item);
        return item;
    }

    @Override
    public void changePriority(T item, double priority) {
        // O(1)
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        // O(1)
        double oldPriority = itemToPriority.get(item);
        if (priority != oldPriority) {
            // O(logN)
            Set<T> itemsWithOldPriority = priorityToItem.get(oldPriority);
            // O(1)
            itemsWithOldPriority.remove(item);
            if (itemsWithOldPriority.isEmpty()) {
                priorityToItem.remove(oldPriority);
            }
            itemToPriority.remove(item);
            // O(logN)
            add(item, priority);
        }
    }

    @Override
    public int size() {
        return itemToPriority.size();
    }

    /**
     * Returns any one element from the given iterable.
     *
     * @param it the iterable of elements.
     * @return any one element from the given iterable.
     */
    private T firstOf(Iterable<T> it) {
        return it.iterator().next();
    }
}
