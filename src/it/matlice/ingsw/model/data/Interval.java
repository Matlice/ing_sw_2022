package it.matlice.ingsw.model.data;

import it.matlice.ingsw.model.exceptions.CannotParseIntervalException;
import it.matlice.ingsw.model.exceptions.CannotParseTimeException;
import it.matlice.ingsw.model.exceptions.InvalidIntervalException;
import it.matlice.ingsw.model.exceptions.InvalidTimeException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Interval implements Comparable<Interval> {
    private final Time start;
    private final Time end;

    public Interval(int start, int end) {
        assert start % 30 == 0;
        assert end % 30 == 0;
        assert start <= end;
        assert end <= 60*24;

        this.start = new Time(start);
        this.end = new Time(end);
    }

    public int getStartingMinute() {
        return this.start.getTime();
    }

    public int getEndingMinute() {
        return this.end.getTime();
    }

    /**
     * Ritorna true se l'intervallo si sovrappone ad un altro intervallo
     * @param other l'altro intervallo
     * @return true se si sovrappongono
     */
    private boolean overlaps(Interval other) {
        if (other.start.compareTo(this.end) <= 0 && other.start.compareTo(this.start) >= 0) return true;
        if (other.end.compareTo(this.end) <= 0 && other.end.compareTo(this.start) >= 0) return true;
        if (this.start.compareTo(other.end) <= 0 && this.end.compareTo(other.start) >= 0) return true;
        if (this.end.compareTo(other.end) <= 0 && this.end.compareTo(other.start) >= 0) return true;
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
        return new Interval(
                Math.min(this.start.getTime(), oth.start.getTime()),
                Math.max(this.end.getTime(), oth.end.getTime()));
    }

    @Override
    public String toString() {
        return this.start + "-" + this.end;
    }

    /**
     * Dato una stringa rappresentante un intervallo, ritorna l'istanza dell'intervallo
     * @param intervalString stringa dell'intervallo
     * @return istanza di intervallo
     * @throws CannotParseIntervalException errore durante il parsing della stringa
     * @throws InvalidIntervalException intervallo invalido
     */
    public static Interval fromString(String intervalString) throws CannotParseIntervalException, InvalidIntervalException {
        try {
            var times = intervalString.split("[\\-]");

            if (times.length != 2) throw new CannotParseIntervalException();

            var start = Time.fromString(times[0]);
            var end = Time.fromString(times[1]);

            if (start.getTime() > end.getTime()) throw new InvalidIntervalException();

            return new Interval(start.getTime(), end.getTime());
        } catch (RuntimeException | CannotParseTimeException | InvalidTimeException e) {
            throw new CannotParseIntervalException();
        }
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
        return this.start.compareTo(o.start);
    }

    /**
     * Ritorna true se l'orario specificato è incluso nell'intervallo
     * @param t orario da verificare
     * @return bool
     */
    public boolean includes(Time t) {
        if (t.compareTo(this.start) >= 0 && t.compareTo((this.end)) <= 0) return true;
        return false;
    }

    public static class Time implements Comparable<Time> {
        private final int time; // in minutes after midnight

        public Time(int time) {
            assert time % 30 == 0;
            assert time <= 60*24;
            this.time = time;
        }

        public int getTime() {
            return this.time;
        }

        public int getHour() {
            return this.time / 60;
        }

        public int getMinute() {
            return this.time % 60;
        }

        /**
         * Dato una stringa rappresentante un orario, ritorna l'istanza dell'orario
         * @param timeString stringa dell'orario
         * @return istanza di orario
         * @throws CannotParseTimeException errore durante il parsing della stringa
         * @throws InvalidTimeException orario invalido
         */
        public static Time fromString(String timeString) throws CannotParseTimeException, InvalidTimeException {
            try {
                if (!timeString.contains(":")) throw new CannotParseTimeException();
                if (timeString.split(":")[1].length() != 2) throw new CannotParseTimeException();
                var t = timeToMinutes(timeString);
                if (t % 30 != 0) throw new InvalidTimeException();
                return new Time(t);
            } catch (RuntimeException e) {
                throw new CannotParseTimeException();
            }
        }

        /**
         * Get a time in the format HH:MM and returns the number of minutes from midnight
         * es. from 8:30 returns 510
         * @param time time in the format HH:MM
         * @return number of minutes from midnight
         */
        private static int timeToMinutes(String time) throws CannotParseTimeException {
            if (!time.contains(":")) throw new CannotParseTimeException();
            var hour_part = time.split(":")[0];
            var minute_part = time.split(":")[1];

            var hour = 0;
            try {
                hour = Integer.parseInt(hour_part);
            } catch (RuntimeException e) {
                throw new CannotParseTimeException();
            }

            var minute = 0;
            try {
                minute = Integer.parseInt(minute_part);
            } catch (RuntimeException e) {
                throw new CannotParseTimeException();
            }
            if (hour > 24) throw new CannotParseTimeException();
            if (hour < 0) throw new CannotParseTimeException();
            if (minute >= 60) throw new CannotParseTimeException();
            if (minute < 0) throw new CannotParseTimeException();

            var hour_minute = time.split(":");
            return Integer.parseInt(hour_minute[0]) * 60 + minute;
        }

        @Override
        public String toString() {
            return this.getHour() + ":" + String.format("%02d", this.getMinute());
        }

        @Override
        public int compareTo(@NotNull Time o) {
            if (this.time < o.time) return -1;
            if (this.time > o.time) return 1;
            return 0;
        }

    }

}