package server.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import server.hibernate.models.Empresa;
import server.hibernate.utils.HibernateUtil;

public class EmpresaDao {

    public Empresa get(int id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Empresa.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    public Empresa getByEmail(String email) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from Empresa where email = :email", Empresa.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Empresa getByCnpj(String cnpj) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from Empresa where cnpj = :cnpj", Empresa.class)
                    .setParameter("cnpj", cnpj)
                    .uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void save(Empresa empresa) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.persist(empresa);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e;
        }
    }

    public void update(Empresa empresa) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.merge(empresa);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e;
        }
    }

    public void delete(Empresa empresa) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.remove(empresa);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e;
        }
    }
}