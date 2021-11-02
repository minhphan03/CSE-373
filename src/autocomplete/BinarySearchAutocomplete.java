package autocomplete;

import java.util.*;

/**
 * Binary search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class BinarySearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> terms;

    /**
     * Constructs an empty instance.
     */
    public BinarySearchAutocomplete() {
        this.terms = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        this.terms.addAll(terms);
        Collections.sort(this.terms, CharSequence::compare);
        //throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> result = new ArrayList<>();
        if (prefix == null || prefix.length() == 0) {
            return result;
        }
        int start = Collections.binarySearch(this.terms, prefix, CharSequence::compare);
        if (!this.terms.contains(prefix)) {
            start = -(start + 1);
        }

        if (start < 0) {
            return result;
        }
        for (CharSequence term : terms.subList(start, terms.size())) {
            if (prefix.length() <= term.length()) {
                CharSequence part = term.subSequence(0, prefix.length());
                if (CharSequence.compare(prefix, part) == 0) {
                    result.add(term);
                } else {
                    return result;
                }
            }
        }
        return result;
    }
}
