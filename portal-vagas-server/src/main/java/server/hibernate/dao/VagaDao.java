package server.hibernate.dao;

import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import server.hibernate.models.Competencia;
import server.hibernate.models.Vaga;
import server.hibernate.utils.HibernateUtil;

import java.util.List;
import java.util.stream.Collectors;

public class VagaDao {

    public Vaga get(int id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Vaga.class, id);
        }
    }

    public List<Vaga> fetchAllByEmpresaId(int empresaId) {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("SELECT v FROM Vaga v WHERE v.idEmpresa.id = :empresaId", Vaga.class)
                    .setParameter("empresaId", empresaId)
                    .getResultList();
        }
    }

    public List<Vaga> getVagasByCompetenciasAnd(List<Integer> competenciasIds) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT v FROM Vaga v JOIN FETCH v.requisitos r " +
                    "WHERE r.idCompetencia.id IN :competencias " +
                    "GROUP BY v.id, v.descricao, v.estado, v.faixaSalarial, v.idEmpresa.id, v.nome " +
                    "HAVING COUNT(DISTINCT r.idCompetencia.id) = :competenciasCount";
            Query query = session.createQuery(hql, Vaga.class);
            query.setParameter("competencias", competenciasIds);
            query.setParameter("competenciasCount", (long) competenciasIds.size());
            return query.getResultList();
        }
    }
    public List<Vaga> getVagasByCompetenciasOr(List<Integer> competenciasIds) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "SELECT DISTINCT v FROM Vaga v JOIN FETCH v.requisitos r WHERE r.idCompetencia.id IN :competencias";
            Query query = session.createQuery(hql, Vaga.class);
            query.setParameter("competencias", competenciasIds);
            return query.getResultList();
        }
    }

    public void save(Vaga vaga) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.persist(vaga);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void update(Vaga vaga) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.merge(vaga);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void delete(Vaga vaga) {
        Session session;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.remove(vaga);
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
