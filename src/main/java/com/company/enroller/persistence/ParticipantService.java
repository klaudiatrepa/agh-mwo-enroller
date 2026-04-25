package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import java.util.Collection;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

@Component("participantService")
public class ParticipantService {

  DatabaseConnector connector;

  public ParticipantService() {
    connector = DatabaseConnector.getInstance();
  }

  public Collection<Participant> getAll(String sortBy, String order) {
    String hql = "FROM Participant ORDER BY " + sortBy + " " + order;
    Query query = connector.getSession().createQuery(hql);
    return query.list();
  }

  public Collection<Participant> getAllWhichContainsInLogin(String key) {
    String hql = "FROM Participant WHERE login LIKE :key ";
    Query query = connector.getSession().createQuery(hql);
    query.setParameter("key", "%" + key + "%");
    return query.list();
  }

  public Participant findByLogin(String login) {
    // String hql = "FROM Participant WHERE login = :login";
    // Query query = connector.getSession().createQuery(hql);
    // query.setParameter("login", login);
    // return (Participant) query;

    return (Participant) connector.getSession().get(Participant.class, login); //<- get to wbudowana metod Hibernate która zwraca dane po kluczu (to co w klasie jest pospięte pod @Id)
  }

  public void createParticipant(String login, String password) {
    Participant newParticipant = new Participant();
    newParticipant.setLogin(login);
    newParticipant.setPassword(password);

    Session session = connector.getSession();
    session.beginTransaction();
    session.save(newParticipant);
    session.getTransaction().commit();
  }

  public void deleteParticipant(String login) {
    Participant participantToDelete = (Participant) connector
      .getSession()
      .get(Participant.class, login);

    Session session = connector.getSession();
    session.beginTransaction();
    session.delete(participantToDelete);
    session.getTransaction().commit();
  }

  public void updateParticipant(Participant participant) {
    Session session = connector.getSession();
    session.beginTransaction();
    session.update(participant);
    session.getTransaction().commit();
  }
}
