package app.daos;

import app.daos.interfaces.IDAO;
import app.entities.Candidate;
import app.entities.CandidateSkill;
import app.entities.CandidateSkillId;
import app.entities.Skill;
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
            List<Candidate> list = em.createQuery(
                            "SELECT c FROM Candidate c " +
                                    "LEFT JOIN FETCH c.candidateSkills cs " +
                                    "LEFT JOIN FETCH cs.skill " +
                                    "WHERE c.id = :id", Candidate.class)
                    .setParameter("id", id)
                    .getResultList();
            return list.isEmpty() ? null : list.get(0);
        }
    }

    public List<Candidate> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT c FROM Candidate c " +
                                    "LEFT JOIN FETCH c.candidateSkills cs " +
                                    "LEFT JOIN FETCH cs.skill", Candidate.class)
                    .getResultList();
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

    //US-4
    public List<Candidate> getBySkillCategory(app.entities.enums.SkillCategory category) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT c FROM Candidate c " +
                                    "JOIN FETCH c.candidateSkills cs " +
                                    "JOIN FETCH cs.skill s " +
                                    "WHERE s.category = :cat", Candidate.class)
                    .setParameter("cat", category)
                    .getResultList();
        }
    }
}