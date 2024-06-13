package server.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import server.hibernate.models.Candidato;
import server.hibernate.utils.HibernateUtil;

public class CandidatoDao {

    public Candidato get(int id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Candidato.class, id);
        }
    }

    public Candidato getByEmail(String email) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from Candidato where email = :email", Candidato.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void save(Candidato candidato) {
        Session session;
        Transaction transaction = null;
        try  {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.persist(candidato);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void update(Candidato candidato) {
        Session session;
        Transaction transaction = null;
        try  {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.merge(candidato);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void delete(Candidato candidato) {
        Session session;
        Transaction transaction = null;
        try  {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.remove(candidato);
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
