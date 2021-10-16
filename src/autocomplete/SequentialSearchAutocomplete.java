package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sequential search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class SequentialSearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> terms;

    /**
     * Constructs an empty instance.
     */
    public SequentialSearchAutocomplete() {
        this.terms = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        this.terms.addAll(terms);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> matches = new ArrayList<CharSequence>(); // create new arraylist to return
        for (CharSequence word : terms) {
            if (prefix.length() <= word.length()) {
                String word1 = word.subSequence(0, prefix.length()).toString();
                if (prefix.toString().matches(word1)) {
                    matches.add(word);
                }
            }
        }

        return matches;
    }
}
