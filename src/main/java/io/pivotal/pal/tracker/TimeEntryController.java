package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;


@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(@Qualifier("JdbcTimeEntryRepository") TimeEntryRepository timeEntryRepository) {

        this.timeEntryRepository = timeEntryRepository;
    }


    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {

        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);
        return new ResponseEntity<>(timeEntry,HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {

        TimeEntry entry = timeEntryRepository.find(timeEntryId);
        return (entry==null)?new ResponseEntity<>(entry, HttpStatus.NOT_FOUND):new ResponseEntity<>(entry, HttpStatus.OK);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<>(timeEntryRepository.list(),HttpStatus.OK);
    }
    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        expected.setTimeEntryId(timeEntryId);
        TimeEntry updated = timeEntryRepository.update(timeEntryId, expected);
        return (updated==null)?new ResponseEntity<>(updated,HttpStatus.NOT_FOUND):new ResponseEntity<>(updated,HttpStatus.OK);
    }
    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
