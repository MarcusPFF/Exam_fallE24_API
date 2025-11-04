package app.daos;

import app.daos.interfaces.IDAO;
import app.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class CandidateDAO implements IDAO<Candidate, Integer> {
    private final EntityManagerFactory emf;

    public CandidateDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Candidate create(Candidate entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        }
    }

    public Candidate findById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Candidate.class, id);
        }
    }

    public List<Candidate> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT c FROM Candidate c", Candidate.class).getResultList();
        }
    }

    public Candidate update(Candidate entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Candidate merged = em.merge(entity);
            em.getTransaction().commit();
            return merged;
        }
    }

    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Candidate ref = em.find(Candidate.class, id);
            if (ref != null) em.remove(ref);
            em.getTransaction().commit();
        }
    }

    public void linkSkill(int candidateId, int skillId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Candidate candidate = em.find(Candidate.class, candidateId);
            Skill skill = em.find(Skill.class, skillId);
            if (candidate == null || skill == null) {
                em.getTransaction().rollback();
                return;
            }

            Long exists = em.createQuery(
                    "SELECT COUNT(cs) FROM CandidateSkill cs WHERE cs.candidate.id = :cid AND cs.skill.id = :sid",
                    Long.class).setParameter("cid", candidateId).setParameter("sid", skillId).getSingleResult();
            if (exists == 0) {
                CandidateSkill cs = CandidateSkill.builder()
                        .id(new CandidateSkillId(candidate.getId(), skill.getId()))
                        .candidate(candidate)
                        .skill(skill)
                        .build();
                em.persist(cs);
                candidate.getCandidateSkills().add(cs);
                skill.getCandidateSkills().add(cs);
            }

            em.getTransaction().commit();
        }
    }
}