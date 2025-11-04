package app.config;

import app.entities.*;
import app.entities.enums.SkillCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Populator {

    private static void link(EntityManager em, Candidate c, Skill s) {
        CandidateSkill cs = CandidateSkill.builder()
                .id(new CandidateSkillId(c.getId(), s.getId()))
                .candidate(c)
                .skill(s)
                .build();
        em.persist(cs);
        c.getCandidateSkills().add(cs);
        s.getCandidateSkills().add(cs);
    }

    public static void seed(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {
            Long skillCount = em.createQuery("SELECT COUNT(s) FROM Skill s", Long.class).getSingleResult();
            Long candidateCount = em.createQuery("SELECT COUNT(c) FROM Candidate c", Long.class).getSingleResult();
            if (skillCount > 0 || candidateCount > 0) return;

            em.getTransaction().begin();

            Skill java = Skill.builder().name("Java").category(SkillCategory.PROG_LANG).description("Number-one-language").build();
            Skill postgres = Skill.builder().name("PostgreSQL").category(SkillCategory.DB).description("Make-database-tables").build();
            Skill docker = Skill.builder().name("Docker").category(SkillCategory.DEVOPS).description("Container platform").build();
            Skill react = Skill.builder().name("React").category(SkillCategory.FRONTEND).description("Frontend library").build();
            Skill junit = Skill.builder().name("JUnit").category(SkillCategory.TESTING).description("Testing framework").build();
            Skill tenserflow = Skill.builder().name("TensorFlow").category(SkillCategory.DATA).description("Data science, analytics, and machine learning tools").build();
            Skill javalin = Skill.builder().name("Javalin").category(SkillCategory.FRAMEWORK).description("Java application framework").build();

            em.persist(java);
            em.persist(postgres);
            em.persist(docker);
            em.persist(react);
            em.persist(junit);
            em.persist(tenserflow);
            em.persist(javalin);

            Candidate marcus = Candidate.builder().name("Marcus Forsberg").phone("123456789").education("EK Datamatiker 3 SEM").build();
            Candidate victor = Candidate.builder().name("Victor Forsberg").phone("987654321").education("EK Datamatiker 3 SEM").build();
            Candidate georgios = Candidate.builder().name("Georgios Papageorgiou").phone("987612345").education("EK Datamatiker 3 SEM").build();

            em.persist(marcus);
            em.persist(victor);
            em.persist(georgios);
            em.flush();

            link(em, marcus, java);
            link(em, marcus, react);
            link(em, marcus, junit);

            link(em, victor, java);
            link(em, victor, javalin);
            link(em, victor, postgres);
            link(em, victor, docker);

            link(em, georgios, tenserflow);
            link(em, georgios, postgres);

            em.getTransaction().commit();
        }
    }
}
