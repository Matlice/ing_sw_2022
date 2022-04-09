package it.matlice.ingsw.model.data;

import it.matlice.ingsw.model.exceptions.CannotParseDayException;

import java.text.Normalizer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Settings {
    public abstract String getCity();

    public abstract int getDue();

    public abstract List<String> getLocations();

    public abstract List<Interval> getIntervals();

    public abstract List<Day> getDays();

    /**
     * Classe che rappresenta i giorni della settimana
     */
    public static enum Day{
        MON("Lunedì", Calendar.MONDAY),
        TUE("Martedì", Calendar.TUESDAY),
        WED("Mercoledì", Calendar.WEDNESDAY),
        THU("Giovedì", Calendar.THURSDAY),
        FRI("Venerdì", Calendar.FRIDAY),
        SAT("Sabato", Calendar.SATURDAY),
        SUN("Domenica", Calendar.SUNDAY);

        Day(String s, int calendar_day) {
            this.str = s;
            this.calendar_day = calendar_day;
        }

        private String str;
        private int calendar_day;

        // mappa che permette di ottenere il giorno (come Enum) da una stringa
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

        /**
         * Permette di parsare un giorno a partire da una stringa
         * @param day rappresentazione come stringa del giorno
         * @return giorno parsato
         * @throws CannotParseDayException errore durante il parsing
         */
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

        /**
         * Ritorna la rappresentazione (in italiano) del giorno
         * @return
         */
        public String getName() {
            return this.str;
        }

        /**
         * Ritorna l'intero corrispondente al giorno
         * per la libreria java.util.Calendar
         * @return
         */
        public int getCalendarDay() {
            return this.calendar_day;
        }
    }


}
