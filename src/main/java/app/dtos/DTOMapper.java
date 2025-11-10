package app.dtos;

import app.entities.Candidate;
import app.entities.CandidateSkill;
import app.entities.Skill;

import java.util.ArrayList;
import java.util.List;

public final class DTOMapper {
    private DTOMapper() {
    }

    public static SkillDTO toSkillDTO(Skill e) {
        if (e == null) return null;
        return new SkillDTO(e.getId(),
                e.getName(),
                e.getSlug(),
                e.getCategory(),
                e.getDescription(),
                null,
                null);
    }

    public static List<SkillDTO> toSkillDTOs(List<Skill> entities) {
        if (entities == null || entities.isEmpty())
            return List.of();
        List<SkillDTO> out = new ArrayList<>(entities.size());
        for (Skill s : entities) out.add(toSkillDTO(s));
        return out;
    }

    public static CandidateDTO toCandidateDTO(Candidate e) {
        if (e == null) return null;
        List<SkillDTO> skills = new ArrayList<>();
        if (e.getCandidateSkills() != null) {
            for (CandidateSkill cs : e.getCandidateSkills()) {
                skills.add(toSkillDTO(cs.getSkill()));
            }
        }
        return new CandidateDTO(e.getId(), e.getName(), e.getPhone(), e.getEducation(), skills);
    }

    public static List<CandidateDTO> toCandidateDTOs(List<Candidate> entities) {
        if (entities == null || entities.isEmpty()) return List.of();
        List<CandidateDTO> out = new ArrayList<>(entities.size());
        for (Candidate c : entities)
            out.add(toCandidateDTO(c));
        return out;
    }
}