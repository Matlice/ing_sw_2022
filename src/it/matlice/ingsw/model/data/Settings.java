package it.matlice.ingsw.model.data;

import it.matlice.ingsw.model.exceptions.CannotParseDayException;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Settings {
    public abstract String getCity();

    public abstract int getDue();

    public abstract List<String> getLocations();

    public abstract List<Interval> getIntervals();

    public abstract List<Day> getDays();

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
