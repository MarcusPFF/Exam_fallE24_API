package app.services;

import app.daos.CandidateDAO;
import app.dtos.CandidateDTO;
import app.dtos.DTOMapper;
import app.entities.Candidate;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class CandidateService {
    private final CandidateDAO dao;

    public CandidateService(EntityManagerFactory emf) {
        this.dao = new CandidateDAO(emf);
    }

    public CandidateDTO create(CandidateDTO dto) {
        Candidate c = Candidate.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .education(dto.getEducation())
                .build();
        return DTOMapper.toCandidateDTO(dao.create(c));
    }

    public CandidateDTO findById(int id) {
        return DTOMapper.toCandidateDTO(dao.findById(id));
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
}