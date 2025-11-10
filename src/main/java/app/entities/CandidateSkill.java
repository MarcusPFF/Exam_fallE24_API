package app.entities;

import jakarta.persistence.*;
import lombok.*;

//JPA Annotationer
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "candidate_skills")
public class CandidateSkill {
    @EmbeddedId
    private CandidateSkillId id;

    //FetchType.Lazy because we only need to get the relations, when we need them.
    //candidates - why we only use Lazy, we only need the candidates loaded in.
    //Candidates/{id} when we want to get the relations
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("candidateId")
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
}