package it.matlice.ingsw.data;

import it.matlice.ingsw.model.exceptions.CannotParseDayException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public abstract List<Day> getDays();
    public abstract void setDays(List<Day> days) throws Exception;
    public abstract void addDay(Day day) throws Exception;

    public static enum Day{
        MON("Lunedì"),
        TUE("Martedì"),
        WED("Mercoledì"),
        THU("Giovedì"),
        FRI("Venerdì"),
        SAT("Sabato"),
        SUN("Domenica");

        Day(String s) {
            this.str = s;
        }

        private String str;
        public static final Map<String, Day> dayMap = new HashMap<>();

        static {
            dayMap.put("lunedi", Settings.Day.MON);
            dayMap.put("lunedì", Settings.Day.MON);
            dayMap.put("martedi", Settings.Day.TUE);
            dayMap.put("martedì", Settings.Day.TUE);
            dayMap.put("mercoledi", Settings.Day.WED);
            dayMap.put("mercoledì", Settings.Day.WED);
            dayMap.put("giovedi", Settings.Day.THU);
            dayMap.put("giovedì", Settings.Day.THU);
            dayMap.put("venerdi", Settings.Day.FRI);
            dayMap.put("venerdì", Settings.Day.FRI);
            dayMap.put("sabato", Settings.Day.SAT);
            dayMap.put("domenica", Settings.Day.SUN);
        }

        public static Day fromString(String day) throws CannotParseDayException {
            Day r = dayMap.get(day);
            if (r != null) {
                return r;
            } else {
                throw new CannotParseDayException();
            }
        }

        public String getName() {
            return this.str;
        }

    }


}
