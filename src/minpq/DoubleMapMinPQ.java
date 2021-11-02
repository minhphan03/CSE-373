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
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }

        // create a new key-pair value <Priority,Set> because items can be in the same priority level
        // TreeMap is proficient in retrieving priority levels (ordered data structure)
        if (!priorityToItem.containsKey(priority)) {
            priorityToItem.put(priority, new HashSet<>());
        }
        // get the Set that contains the priority level and add the item into the set for searching purposes
        Set<T> itemsWithPriority = priorityToItem.get(priority);
        itemsWithPriority.add(item);

        //the HashMap helps to access the priority quickly of an item.
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
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        double minPriority = priorityToItem.firstKey();
        Set<T> itemsWithMinPriority = priorityToItem.get(minPriority);
        T item = firstOf(itemsWithMinPriority);
        itemsWithMinPriority.remove(item);
        if (itemsWithMinPriority.isEmpty()) {
            priorityToItem.remove(minPriority);
        }
        itemToPriority.remove(item);
        return item;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        double oldPriority = itemToPriority.get(item);
        if (priority != oldPriority) {
            Set<T> itemsWithOldPriority = priorityToItem.get(oldPriority);
            itemsWithOldPriority.remove(item);
            if (itemsWithOldPriority.isEmpty()) {
                priorityToItem.remove(oldPriority);
            }
            itemToPriority.remove(item);
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
