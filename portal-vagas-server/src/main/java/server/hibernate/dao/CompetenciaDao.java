package server.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import server.hibernate.models.Competencia;
import server.hibernate.utils.HibernateUtil;

public class CompetenciaDao {

    public Competencia get(int id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Competencia.class, id);
        }
    }

    public Competencia getByName(String nome) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from Competencia where nome = :nome", Competencia.class)
                    .setParameter("nome", nome)
                    .uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void save(Competencia competencia) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.persist(competencia);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void update(Competencia competencia) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.merge(competencia);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void delete(Competencia competencia) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.remove(competencia);
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