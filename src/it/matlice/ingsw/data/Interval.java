package it.matlice.ingsw.data;

public class Interval {
    private final int start;
    private final int end;

    public Interval(int start, int end) {
        assert start % 30 == 0;
        assert end % 30 == 0;
        assert start < end;
        assert end < 60*24;

        this.start = start;
        this.end = end;
    }

    public int getStartingMinute() {
        return this.start;
    }

    public int getEndingMinute() {
        return this.end;
    }

}
