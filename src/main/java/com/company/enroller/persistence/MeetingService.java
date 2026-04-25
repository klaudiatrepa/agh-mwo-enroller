package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

  DatabaseConnector connector;

  public MeetingService() {
    connector = DatabaseConnector.getInstance();
  }

  public Collection<Meeting> getAll() {
    String hql = "FROM Meeting";
    Query query = connector.getSession().createQuery(hql);
    return query.list();
  }

  public Meeting findById(long id) {
    return (Meeting) connector.getSession().get(Meeting.class, id);
  }

  public void createMeeting(
    String title,
    String description,
    String date
  ) {
    Meeting newMeeting = new Meeting();
    newMeeting.setTitle(title);
    newMeeting.setDescription(description);
    newMeeting.setDate(date);

    Session session = connector.getSession();
    session.beginTransaction();
    session.save(newMeeting);
    session.getTransaction().commit();
  }

  public void deleteMeeting(long id) {
    Meeting meetingToDelete = (Meeting) connector
      .getSession()
      .get(Meeting.class, id);

    Session session = connector.getSession();
    session.beginTransaction();
    session.delete(meetingToDelete);
    session.getTransaction().commit();
  }

  public void updateMeeting(Meeting meeting) {
    Session session = connector.getSession();
    session.beginTransaction();
    session.update(meeting);
    session.getTransaction().commit();
  }

  public void addParticipants(Meeting meeting, Collection<Participant> participants){
    Session session = connector.getSession();
    session.beginTransaction();
    for (Participant participant : participants) {
      meeting.addParticipant(participant);
    }
    session.update(meeting);
    session.getTransaction().commit();
  }

  public void deleteParticipantFromMeeting(Meeting meeting, Participant participant){
    Session session = connector.getSession();
    session.beginTransaction();
    meeting.removeParticipant(participant);
    session.update(meeting);
    session.getTransaction().commit();
  }
}
