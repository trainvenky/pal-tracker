package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private MysqlDataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private RowMapper<TimeEntry> rm;

    public JdbcTimeEntryRepository(MysqlDataSource dataSource) {
        this.dataSource=dataSource;
        this.jdbcTemplate=new JdbcTemplate(dataSource);
        rm = new RowMapper<TimeEntry>() {
            @Override
            public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                TimeEntry t = new TimeEntry();
                t.setTimeEntryId(rs.getLong("id"));
                t.setProjectId(rs.getLong("project_id"));
                t.setHours(rs.getInt("hours"));
                t.setUserId(rs.getLong("user_id"));
                t.setDate(rs.getDate("date").toLocalDate());
                return t;
            }
        };
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement s = con.prepareStatement("INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?,?,?,?)",RETURN_GENERATED_KEYS);
            s.setLong(1,timeEntry.getProjectId());
            s.setLong(2,timeEntry.getUserId());
            s.setDate(3, Date.valueOf(timeEntry.getDate()));
            s.setInt(4,timeEntry.getHours());

            return s;
        },generatedKeyHolder);

        timeEntry.setTimeEntryId(Long.parseLong(generatedKeyHolder.getKey().toString()));


        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        Object[] args = new Object[] {timeEntryId};
        int[] types = new int[] {Types.BIGINT};
        List<TimeEntry> timeEntryList = jdbcTemplate.query("SELECT * FROM time_entries where id=?",args,types, rm);

        return timeEntryList.size()==0 ? null : timeEntryList.get(0);
    }

    @Override
    public List<TimeEntry> list() {

        Object[] args = null;
        int[] types = null;
        List<TimeEntry> timeEntryList = jdbcTemplate.query("SELECT * FROM time_entries",args,types, rm);
        return timeEntryList;
    }

    @Override
    public TimeEntry update(long eq, TimeEntry timeEntry) {

        jdbcTemplate.update(con -> {
            PreparedStatement s = con.prepareStatement("UPDATE time_entries SET project_id=?, user_id=?, date=?, hours=? WHERE id=?");

            s.setLong(1,timeEntry.getProjectId());
            s.setLong(2,timeEntry.getUserId());
            s.setDate(3, Date.valueOf(timeEntry.getDate()));
            s.setInt(4,timeEntry.getHours());
            s.setLong(5,eq);
            return s;
        });

        return find(eq);
    }

    @Override
    public void delete(long timeEntryId) {
        jdbcTemplate.update(con -> {
            PreparedStatement s = con.prepareStatement("DELETE FROM time_entries WHERE id=?");

            s.setLong(1,timeEntryId);

            return s;
        });
    }
}
