package com.company.enroller.controllers;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

  private final String ASC = "ASC";
  private final String DESC = "DESC";
  private final String LOGIN = "login";

  @Autowired
  ParticipantService participantService;

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<?> getParticipants(
    @RequestParam(defaultValue = LOGIN) String sortBy,
    @RequestParam(defaultValue = ASC) String order,
    @RequestParam(required = false) String key
  ) {
    if ((!order.equals(ASC) && !order.equals(DESC)) || !sortBy.equals(LOGIN)) {
      return new ResponseEntity("Incorrect params values", HttpStatus.CONFLICT);
    }

    Collection<Participant> participants;

    if (key != null) {
      participants = participantService.getAllWhichContainsInLogin(key);

      if (participants.isEmpty()) {
        return new ResponseEntity("No data found.", HttpStatus.NOT_FOUND);
      }
    } else participants = participantService.getAll(sortBy, order);

    return new ResponseEntity<Collection<Participant>>(
      participants,
      HttpStatus.OK
    );
  }

  @RequestMapping(value = "/{login}", method = RequestMethod.GET)
  public ResponseEntity<?> getParticipant(@PathVariable("login") String login) {
    Participant participant = participantService.findByLogin(login);
    if (participant == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<Participant>(participant, HttpStatus.OK);
  }

  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<?> registerParticipant(
    @RequestBody Participant participant
  ) {
    Participant foundParticipant = participantService.findByLogin(
      participant.getLogin()
    );
    if (foundParticipant != null) {
      return new ResponseEntity(
        "Unable to create. A participant with login " +
          participant.getLogin() +
          " already exist.",
        HttpStatus.CONFLICT
      );
    }
    participantService.createParticipant(
      participant.getLogin(),
      participant.getPassword()
    );
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @RequestMapping(value = "", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteParticipant(
    @RequestBody Participant participant
  ) {
    Participant foundParticipant = participantService.findByLogin(
      participant.getLogin()
    );
    if (foundParticipant == null) {
      return new ResponseEntity(
        "Unable to delete. A participant with login " +
          participant.getLogin() +
          " does not exist.",
        HttpStatus.CONFLICT
      );
    }
    participantService.deleteParticipant(participant.getLogin());
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @RequestMapping(value = "/{login}", method = RequestMethod.PUT)
  public ResponseEntity<?> updateParticipant(
    @PathVariable("login") String login,
    @RequestBody Participant participant
  ) {
    Participant foundParticipant = participantService.findByLogin(login);
    if (foundParticipant == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    foundParticipant.setPassword(participant.getPassword());
    participantService.updateParticipant(foundParticipant);
    return new ResponseEntity(HttpStatus.OK);
  }
}
