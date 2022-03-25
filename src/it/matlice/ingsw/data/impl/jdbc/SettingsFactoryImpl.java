package it.matlice.ingsw.data.impl.jdbc;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import it.matlice.ingsw.data.Interval;
import it.matlice.ingsw.data.Settings;
import it.matlice.ingsw.data.factories.SettingsFactory;
import it.matlice.ingsw.data.impl.jdbc.db.*;
import it.matlice.ingsw.data.impl.jdbc.types.SettingsImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class SettingsFactoryImpl implements SettingsFactory {

    private final Dao<SettingsDB, Integer> settingsDAO;
    private final Dao<LocationsDB, Integer> locationsDAO;
    private final Dao<DaysDB, Integer> daysDAO;
    private final Dao<IntervalsDB, Integer> intervalsDAO;

    public SettingsFactoryImpl() throws SQLException {
        var connectionSource = JdbcConnection.getInstance().getConnectionSource();
        this.settingsDAO = DaoManager.createDao(connectionSource, SettingsDB.class);
        this.locationsDAO = DaoManager.createDao(connectionSource, LocationsDB.class);
        this.daysDAO = DaoManager.createDao(connectionSource, DaysDB.class);
        this.intervalsDAO = DaoManager.createDao(connectionSource, IntervalsDB.class);

        if (!this.settingsDAO.isTableExists())
            TableUtils.createTable(connectionSource, SettingsDB.class);
        if (!this.locationsDAO.isTableExists())
            TableUtils.createTable(connectionSource, LocationsDB.class);
        if (!this.daysDAO.isTableExists())
            TableUtils.createTable(connectionSource, DaysDB.class);
        if (!this.intervalsDAO.isTableExists())
            TableUtils.createTable(connectionSource, IntervalsDB.class);
    }

    @Override
    public Settings readSettings() throws SQLException {
        var settings = settingsDAO.queryForAll();
        if(settings.size() == 0)
            return null; //todo il configuratore deve essere obbligato a fare i settings quando la loro lettura fallisce.
        var setting = settings.get(0);
        var locations = locationsDAO.query(this.locationsDAO.queryBuilder().where().eq("ref_id", setting.getId()).prepare());
        var days = daysDAO.query(this.daysDAO.queryBuilder().where().eq("ref_id", setting.getId()).prepare());
        var intervals = intervalsDAO.query(this.intervalsDAO.queryBuilder().where().eq("ref_id", setting.getId()).prepare());
        return new SettingsImpl(this, setting, locations, intervals, days);
    }

    @Override
    public Settings makeSettings(String city, int due, @NotNull List<String> locations, @NotNull List<Settings.Day> days, @NotNull List<Interval> intervals) throws SQLException {
        var settings = new SettingsDB(city, due);
        this.settingsDAO.create(settings);

        for (var loc: locations)
            this.addLocation(settings, loc);
        for (var d: days)
            this.addDay(settings, d);
        for (var intv: intervals)
            this.addInterval(settings, intv);

        return this.readSettings();
    }

    private void addLocation(SettingsDB db, String l) throws SQLException {
        this.locationsDAO.create(new LocationsDB(db, l));
    }

    private void addDay(SettingsDB db, Settings.Day d) throws SQLException {
        this.daysDAO.create(new DaysDB(db, d));
    }

    private void addInterval(SettingsDB db, Interval i) throws SQLException {
        //todo check for overlapping intervals and stuff...
        this.intervalsDAO.create(new IntervalsDB(db, i));
    }

    private void removeLocations(SettingsDB db) throws SQLException {
        this.locationsDAO.query(this.locationsDAO.deleteBuilder().where().eq("ref_id", db.getId()).prepare());
    }

    private void removeDays(SettingsDB db) throws SQLException {
        this.daysDAO.query(this.daysDAO.deleteBuilder().where().eq("ref_id", db.getId()).prepare());
    }

    private void removeIntervals(SettingsDB db) throws SQLException {
        this.intervalsDAO.query(this.intervalsDAO.deleteBuilder().where().eq("ref_id", db.getId()).prepare());

    }

    public void addLocation(Settings db, String l) throws SQLException {
        assert db instanceof SettingsImpl;
        this.addLocation(((SettingsImpl) db).getDbData(), l);
    }

    public void addDay(Settings db, Settings.Day d) throws SQLException {
        assert db instanceof SettingsImpl;
        this.addDay(((SettingsImpl) db).getDbData(), d);
    }

    public void addInterval(Settings db, Interval i) throws SQLException {
        assert db instanceof SettingsImpl;
        this.addInterval(((SettingsImpl) db).getDbData(), i);
    }

    public void removeLocations(Settings db) throws SQLException {
        assert db instanceof SettingsImpl;
        this.removeLocations(((SettingsImpl) db).getDbData());
    }

    public void removeDays(Settings db) throws SQLException {
        assert db instanceof SettingsImpl;
        this.removeDays(((SettingsImpl) db).getDbData());
    }

    public void removeIntervals(Settings db) throws SQLException {
        assert db instanceof SettingsImpl;
        this.removeIntervals(((SettingsImpl) db).getDbData());
    }

}
