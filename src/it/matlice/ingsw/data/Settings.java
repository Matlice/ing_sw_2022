package it.matlice.ingsw.data;

import java.sql.SQLException;
import java.util.List;

public abstract class Settings {
    public abstract String getCity();

    public abstract int getDue();
    public abstract void setDue(int due);

    public abstract List<String> getLocations();
    public abstract void setLocations(List<String> locations) throws Exception;
    public abstract void addLocation(String location) throws Exception;

    public abstract List<Interval> getIntervals();
    public abstract void setIntervals(List<Interval> intervals) throws Exception;
    public abstract void addInterval(Interval interval) throws Exception;

    public abstract List<Day> getDay();
    public abstract void setDays(List<Day> days) throws Exception;
    public abstract void addDay(Day day) throws Exception;

    public static enum Day{
        MON, TUE, WED, THU, FRI, SAT, SUN
    }


}
