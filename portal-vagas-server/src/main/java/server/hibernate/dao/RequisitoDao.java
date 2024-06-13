package server.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import server.hibernate.models.Requisito;
import server.hibernate.utils.HibernateUtil;

import java.util.List;

public class RequisitoDao {

    public Requisito get(int id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Requisito.class, id);
        }
    }

    public List<Requisito> fetchAllByVagaId(int vagaId) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("SELECT r FROM Requisito r WHERE r.idVaga.id = :vagaId", Requisito.class)
                    .setParameter("vagaId", vagaId)
                    .getResultList();
        }
    }

    public void deleteAllByVagaId(int vagaId) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Requisito r WHERE r.idVaga.id = :vagaId")
                    .setParameter("vagaId", vagaId)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void save(Requisito requisito) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.persist(requisito);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void update(Requisito requisito) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.merge(requisito);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void delete(Requisito requisito) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.remove(requisito);
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
