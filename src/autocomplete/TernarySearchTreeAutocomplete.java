package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;

    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        for (CharSequence word: terms) {
            this.add(word.toString());
        }
        // throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List <CharSequence> matches = new ArrayList<>();
        if (prefix.length() == 0) {
            return matches;
        }

        return keysWithPrefix(prefix.toString());
        // throw new UnsupportedOperationException("Not implemented yet");
    }

    /** Returns true if and only if this TST contains the given key. */
    public boolean contains(String key) {
        if (key == null) {
            throw new NullPointerException("calls contains() with null argument");
        } else if (key.length() == 0) {
            throw new IllegalArgumentException("key must have length >= 1");
        }
        Node node = get(overallRoot, key, 0);
        return node != null && node.isTerm;
    }

    // Private helper method for returning the node (subtree) corresponding to given key
    private Node get(Node node, String key, int depth) {
        if (node == null) {
            return null;
        }
        char curr = key.charAt(depth);
        if (curr < node.data) {
            // if curr comes before node.data, traverse down the left subtree
            return get(node.left, key, depth);
        } else if (curr > node.data) {
            // if curr comes after node.data, traverse down the right subtree
            return get(node.right, key, depth);
        } else if (depth < key.length() - 1) {
            // if curr is the correct letter, and not at the end of the tree, traverse down the middle subtree
            return get(node.mid, key, depth + 1);
        } else {
            // if at the end of the tree path, return the current letter
            return node;
        }
    }

    /** Inserts the string into the TST. */
    public void add(String key) {
        if (key == null) {
            throw new NullPointerException("calls add() with null key");
        }
        // make sure that there is no duplicates
        if (!contains(key)) {
            overallRoot = add(overallRoot, key, 0);
        }
    }

    // Private helper method for adding a node if it doesn't already exist in the TST.
    private Node add(Node node, String key, int depth) {
        char curr = key.charAt(depth);
        if (node == null) {
            node = new Node(curr, false);
        }
        if (curr < node.data) {
            // if curr comes alphabetically before node.data, traverse down the left subtree
            node.left = add(node.left, key, depth);
        } else if (curr > node.data) {
            // if curr comes alphabetically after node.data, traverse down the right subtree
            node.right = add(node.right, key, depth);
        } else if (depth < key.length() - 1) {
            // if curr is the correct letter, and not at the end of the tree, traverse down the middle subtree
            node.mid = add(node.mid, key, depth + 1);
        } else {
            // if at the end of the tree path, ensure isTerm is true
            node.isTerm = true;
        }
        return node;
    }

    public List<CharSequence> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new NullPointerException("calls keysWithPrefix() with null argument");
        } else if (prefix.length() == 0) {
            throw new IllegalArgumentException("prefix must have length >= 1");
        }
        List<CharSequence> list = new LinkedList<>();
        Node node = get(overallRoot, prefix, 0);
        if (node == null) {
            return list;
        }
        if (node.isTerm) {
            list.add(prefix);
        }
        collect(node.mid, new StringBuilder(prefix), list);
        return list;
    }

    private void collect(Node node, StringBuilder prefix, List<CharSequence> list) {
        if (node == null) {
            return;
        }
        collect(node.left, prefix, list);
        if (node.isTerm) {
            list.add(prefix.toString() + node.data);
        }
        prefix.append(node.data);
        collect(node.mid, prefix, list);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(node.right, prefix, list);
    }
    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data, boolean isTerm) {
            this.data = data;
            this.isTerm = isTerm;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }
}
