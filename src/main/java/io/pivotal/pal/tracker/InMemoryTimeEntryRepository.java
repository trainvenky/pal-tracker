package io.pivotal.pal.tracker;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private Map<Long, TimeEntry> timeEntryDb = new HashMap<>();
    private long index=0;

    public InMemoryTimeEntryRepository()
    {

    }
    public TimeEntry create(TimeEntry timeEntry) {
        index++;
        long timeEntryId=index;
        timeEntry.setTimeEntryId(timeEntryId);
        timeEntryDb.put(timeEntryId, timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long timeEntryId) {

        if(timeEntryDb.containsKey(timeEntryId))
            return timeEntryDb.get(timeEntryId);
        else
            return null;
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntryDb.values());

    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        if(timeEntryDb.containsKey(id))
        {
            timeEntryDb.put(id,timeEntry);
            return timeEntry;
        }
        else
            return null;
    }

    public void delete(long id) {
        if(timeEntryDb.containsKey(id))
        {
            timeEntryDb.remove(id);
        }
    }
}
