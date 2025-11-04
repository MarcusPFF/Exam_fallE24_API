package app.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CandidateSkillId implements Serializable {
    private Integer candidateId;
    private Integer skillId;
}