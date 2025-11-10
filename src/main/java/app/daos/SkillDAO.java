package app.daos;

import app.daos.interfaces.IDAO;
import app.entities.Skill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SkillDAO implements IDAO<Skill, Integer> {
    private final EntityManagerFactory emf;

    public SkillDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //CRUD (Create, Read, Update, Delete
    public Skill create(Skill entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        }
    }

    public Skill findById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Skill.class, id);
        }
    }

    public List<Skill> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT s FROM Skill s", Skill.class).getResultList();
        }
    }

    public Skill update(Skill entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Skill merged = em.merge(entity);
            em.getTransaction().commit();
            return merged;
        }
    }

    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Skill ref = em.find(Skill.class, id);
            if (ref != null)
                em.remove(ref);
            em.getTransaction().commit();
        }
    }
}