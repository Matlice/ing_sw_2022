package it.matlice.ingsw.model.data;

import it.matlice.ingsw.model.exceptions.CannotParseDayException;

import java.text.Normalizer;
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
            for(var d: Day.values()) {
                // adds to the map the normalized day string
                dayMap.put(
                        Normalizer.normalize(
                                d.getName().toLowerCase(),
                                Normalizer.Form.NFD)
                            .replaceAll("[\\p{InCombiningDiacriticalMarks}]", ""),
                        d);
            }
        }

        public static Day fromString(String day) throws CannotParseDayException {
            // to lower case and strip accents
            String normalizedDay = Normalizer.normalize(day.toLowerCase(), Normalizer.Form.NFD);
            normalizedDay = normalizedDay.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
            Day r = dayMap.get(normalizedDay);
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
