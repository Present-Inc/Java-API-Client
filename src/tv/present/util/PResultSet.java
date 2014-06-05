package tv.present.util;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Present Result Set
 * June 02, 2014
 * @author Kyle Weisel (kyle@present.tv)
 */
public final class PResultSet<Z> {

    private static final String TAG = "tv.present.util.PResultSet";
    private static final Logger PLog = Logger.getLogger(TAG);

    private int cursor;
    private ArrayList<Z> results;

    /**
     * Constructs a PResultSet.
     * @param cursor is an integer cursor that marks where you left off.
     * @param resultsArray is an ArrayList of a PObject (well, really anything).
     */
    public PResultSet(final int cursor, final ArrayList<Z> resultsArray) {
        this.results = resultsArray;
        this.cursor = cursor;
        PLog.info("Created PResultSet with a resultArray size of " + resultsArray.size());
    }

    /**
     * Gets the cursor value.
     * @return the cursor value as an integer.
     */
    @SuppressWarnings("unused")
    public final int getCursor() {
        return this.cursor;
    }

    /**
     * Gets the generic type results array.
     * @return an ArrayList<T> of objects.
     */
    @SuppressWarnings("unused")
    public final ArrayList<Z> getResults() {
        return this.results;
    }

    /**
     * Adds a generic result to the array.
     * @param result is a generic type.
     */
    @SuppressWarnings("unused")
    public final void addResult(Z result) {
        this.results.add(result);
    }

}