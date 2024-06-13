package server.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import server.hibernate.models.Experiencia;
import server.hibernate.utils.HibernateUtil;

import java.util.List;

public class ExperienciaDao {

    public Experiencia get(int id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Experiencia.class, id);
        }
    }

    public List<Experiencia> fetchAllbyCandidatoId(int id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("SELECT e FROM Experiencia e WHERE e.idCandidato.id = :id", Experiencia.class)
                    .setParameter("id", id)
                    .getResultList();
        }
    }

    public Experiencia getByCandidatoIdAndCompetenciaId(int idCandidato, int idCompetencia) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("SELECT e FROM Experiencia e WHERE e.idCandidato.id = :idCandidato AND e.idCompetencia.id = :idCompetencia", Experiencia.class)
                    .setParameter("idCandidato", idCandidato)
                    .setParameter("idCompetencia", idCompetencia)
                    .uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void save(Experiencia experiencia) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.persist(experiencia);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void update(Experiencia experiencia) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.merge(experiencia);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void delete(Experiencia experiencia) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.remove(experiencia);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            throw e;
        }
    }
}