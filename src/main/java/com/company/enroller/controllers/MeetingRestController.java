package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

  @Autowired
  MeetingService meetingService;

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<?> getMeetings() {
    Collection<Meeting> meetings = meetingService.getAll();

    return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
    Meeting meeting = meetingService.findById(id);
    if (meeting == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
  }

  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
    meetingService.createMeeting(
      meeting.getTitle(),
      meeting.getDescription(),
      meeting.getDate()
    );
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @RequestMapping(value = "", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteMeeting(@RequestBody Meeting meeting) {
    Meeting foundMeeting = meetingService.findById(meeting.getId());
    if (foundMeeting == null) {
      return new ResponseEntity(
        "Unable to delete. A meeting with id " +
          meeting.getId() +
          " does not exist.",
        HttpStatus.CONFLICT
      );
    }
    meetingService.deleteMeeting(meeting.getId());
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public ResponseEntity<?> updateMeetingDetails(
    @PathVariable("id") long id,
    @RequestBody Meeting meeting
  ) {
    Meeting foundMeeting = meetingService.findById(id);
    if (foundMeeting == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    foundMeeting.setTitle(meeting.getTitle());
    foundMeeting.setDescription(meeting.getDescription());
    foundMeeting.setDate(meeting.getDate());
    meetingService.updateMeeting(foundMeeting);
    return new ResponseEntity(HttpStatus.OK);
  }
}
