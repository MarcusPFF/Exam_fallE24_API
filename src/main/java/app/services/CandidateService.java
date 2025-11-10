package app.services;

import app.daos.CandidateDAO;
import app.dtos.CandidateDTO;
import app.dtos.DTOMapper;
import app.dtos.SkillDTO;
import app.entities.Candidate;
import app.services.fetchtools.FetchTools;
import jakarta.persistence.EntityManagerFactory;

import java.util.*;

public class CandidateService {
    private final CandidateDAO dao;
    private final FetchSlugsFromApi stats;

    public CandidateService(EntityManagerFactory emf) {
        this.dao = new CandidateDAO(emf);
        //US-5
        this.stats = new FetchSlugsFromApi(new FetchTools());
    }

    public CandidateDTO create(CandidateDTO dto) {
        Candidate c = Candidate.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .education(dto.getEducation())
                .build();
        return DTOMapper.toCandidateDTO(dao.create(c));
    }

    //US-5
    public CandidateDTO findById(int id) {
        Candidate c = dao.findById(id);
        if (c == null)
            return null;

        CandidateDTO dto = DTOMapper.toCandidateDTO(c);
        List<SkillDTO> skills = dto.getSkills();
        if (skills == null || skills.isEmpty())
            return dto;

        FetchSlugsFromApi.SkillStatsResponse res = new FetchSlugsFromApi(new FetchTools()).FetchSlugsFromUrl();
        if (res == null || res.data == null || res.data.isEmpty())
            return dto;

        for (SkillDTO s : skills) {
            String slug = s.getSlug();
            if (slug == null || slug.isBlank()) continue;
            String key = slug.toLowerCase();
            for (FetchSlugsFromApi.SkillData d : res.data) {
                if (d.slug != null && key.equals(d.slug.toLowerCase())) {
                    s.setPopularityScore(d.popularityScore);
                    s.setAverageSalary(d.averageSalary);
                    break;
                }
            }
        }
        return dto;
    }

    public List<CandidateDTO> getAll() {
        return DTOMapper.toCandidateDTOs(dao.getAll());
    }

    public CandidateDTO update(int id, CandidateDTO dto) {
        Candidate c = Candidate.builder()
                .id(id)
                .name(dto.getName())
                .phone(dto.getPhone())
                .education(dto.getEducation())
                .build();
        return DTOMapper.toCandidateDTO(dao.update(c));
    }

    public void delete(int id) {
        dao.delete(id);
    }

    public void linkSkill(int candidateId, int skillId) {
        dao.linkSkill(candidateId, skillId);
    }

    //US-4
    public List<CandidateDTO> getByCategory(app.entities.enums.SkillCategory category) {
        return DTOMapper.toCandidateDTOs(dao.getBySkillCategory(category));
    }

}