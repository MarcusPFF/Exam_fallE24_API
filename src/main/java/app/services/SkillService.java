package app.services;

import app.daos.SkillDAO;
import app.dtos.DTOMapper;
import app.dtos.SkillDTO;
import app.entities.Skill;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SkillService {
    private final SkillDAO dao;

    public SkillService(EntityManagerFactory emf) {
        this.dao = new SkillDAO(emf);
    }

    public SkillDTO create(SkillDTO dto) {
        Skill s = Skill.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .build();
        return DTOMapper.toSkillDTO(dao.create(s));
    }

    public SkillDTO findById(int id) {
        return DTOMapper.toSkillDTO(dao.findById(id));
    }

    public List<SkillDTO> getAll() {
        return DTOMapper.toSkillDTOs(dao.getAll());
    }

    public SkillDTO update(int id, SkillDTO dto) {
        Skill s = Skill.builder()
                .id(id)
                .name(dto.getName())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .build();
        return DTOMapper.toSkillDTO(dao.update(s));
    }

    public void delete(int id) {
        dao.delete(id);
    }
}