package it.matlice.ingsw.data;

import it.matlice.ingsw.model.exceptions.CannotParseIntervalException;
import it.matlice.ingsw.model.exceptions.InvalidIntervalException;

public class Interval {
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

    public static Interval fromString(String lastInterval) throws CannotParseIntervalException, InvalidIntervalException {
        try {
            var times = lastInterval.split("[\\-]");
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

}
