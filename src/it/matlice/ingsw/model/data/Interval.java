package it.matlice.ingsw.model.data;

import it.matlice.ingsw.model.exceptions.CannotParseIntervalException;
import it.matlice.ingsw.model.exceptions.InvalidIntervalException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Interval implements Comparable<Interval> {
    private final int start;
    private final int end;

    public Interval(int start, int end) {
        assert start % 30 == 0;
        assert end % 30 == 0;
        assert start <= end;
        assert end <= 60*24;

        this.start = start;
        this.end = end;
    }

    public int getStartingMinute() {
        return this.start;
    }

    public int getEndingMinute() {
        return this.end;
    }

    /**
     * Ritorna true se l'intervallo si sovrappone ad un altro intervallo
     * @param other l'altro intervallo
     * @return true se si sovrappongono
     */
    private boolean overlaps(Interval other) {
        if (other.start <= this.end && other.start >= this.start) return true;
        if (other.end <= this.end && other.end >= this.start) return true;
        if (this.start <= other.end && this.end >= other.start) return true;
        if (this.end <= other.end && this.end >= other.start) return true;
        return false;
    }

    /**
     * Ritorna un intervallo che comprende entrambi gli intervalli
     * È necessario garantire che i due intervalli siano sovrapposti
     * @param oth l'altro intervallo da unire
     * @return un nuovo intervallo
     */
    private Interval mergeWith(Interval oth) {
        assert this.overlaps(oth);
        return new Interval(Math.min(this.start, oth.start), Math.max(this.end, oth.end));
    }

    @Override
    public String toString() {
        int start_hour = this.start / 60;
        int start_min = this.start % 60;
        int end_hour = this.end / 60;
        int end_min = this.end % 60;
        return start_hour + ":" + String.format("%02d", start_min) + "-" + end_hour + ":" + String.format("%02d", end_min);
    }

    public static Interval fromString(String intervalString) throws CannotParseIntervalException, InvalidIntervalException {
        try {
            var times = intervalString.split("[\\-]");

            if (times.length != 2) throw new CannotParseIntervalException();
            if (!times[0].contains(":")) throw new CannotParseIntervalException();
            if (!times[1].contains(":")) throw new CannotParseIntervalException();
            if (times[0].split(":")[1].length() != 2) throw new CannotParseIntervalException();
            if (times[1].split(":")[1].length() != 2) throw new CannotParseIntervalException();

            var start = timeToMinutes(times[0]);
            var end = timeToMinutes(times[1]);

            if (start % 30 != 0) throw new InvalidIntervalException();
            if (end % 30 != 0) throw new InvalidIntervalException();
            if (start > end) throw new InvalidIntervalException();
            if (end > 60*24) throw new InvalidIntervalException();

            return new Interval(start, end);
        } catch (RuntimeException e) {
            throw new CannotParseIntervalException();
        }
    }

    /**
     * Get a time in the format HH:MM and returns the number of minutes from midnight
     * es. from 8:30 returns 510
     * @param time time in the format HH:MM
     * @return number of minutes from midnight
     */
    private static int timeToMinutes(String time) {
        assert time.contains(":");

        var hour_minute = time.split(":");
        return Integer.parseInt(hour_minute[0]) * 60 + Integer.parseInt(hour_minute[1]);
    }

    /**
     * Data una lista di intervalli, li riduce nel minimo numero di intervalli
     * @param intervals intervalli in ingresso
     * @return la lista ridotta di intervalli
     */
    public static List<Interval> mergeIntervals(List<Interval> intervals) {

        var merged = true;

        while(merged) {
            // search the two interval to merge
            merged = false;
            int merge_i = -1, merge_j = -1;
            for (int i = 0; i<intervals.size()-1; i++) {
                for (int j = i+1; j<intervals.size(); j++) {
                    if (intervals.get(i).overlaps(intervals.get(j))) {
                        merged = true;
                        merge_i = i;
                        merge_j = j;
                        break;
                    }
                }
                if (merged) break;
            }
            // merge the two intervals
            if (merged) {
                assert merge_j > merge_i;
                Interval merged_interval = intervals.get(merge_i).mergeWith(intervals.get(merge_j));
                intervals.remove(merge_j);
                intervals.remove(merge_i);
                intervals.add(merged_interval);
            }
            // if something has been merged repeat,
            // otherwise exit the loop and return
        }

        return intervals;
    }

    @Override
    public int compareTo(@NotNull Interval o) {
        if (this.start < o.start) return -1;
        if (this.start > o.start) return 1;
        return 0;
    }

}
