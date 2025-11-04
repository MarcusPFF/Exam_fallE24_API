package app.controllers;

import app.dtos.CandidateDTO;
import app.dtos.SkillDTO;
import app.exceptions.ApiException;
import app.services.CandidateService;
import app.services.SkillService;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;

public class CandidateController {
    private final CandidateService candidates;
    private final SkillService skills;

    public CandidateController(EntityManagerFactory emf) {
        this.candidates = new CandidateService(emf);
        this.skills = new SkillService(emf);
    }

    public Handler list() {
        return ctx -> ctx.json(candidates.getAll());
    }

    public Handler getById() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            CandidateDTO dto = candidates.findById(id);
            if (dto == null) throw new ApiException(404, "Candidate not found");
            ctx.json(dto);
        };
    }

    public Handler create() {
        return ctx -> {
            CandidateDTO in = ctx.bodyAsClass(CandidateDTO.class);
            CandidateDTO out = candidates.create(in);
            ctx.status(201).json(out);
        };
    }

    public Handler update() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (candidates.findById(id) == null) throw new ApiException(404, "Candidate not found");
            CandidateDTO in = ctx.bodyAsClass(CandidateDTO.class);
            CandidateDTO out = candidates.update(id, in);
            ctx.json(out);
        };
    }

    public Handler delete() {
        return ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (candidates.findById(id) == null) throw new ApiException(404, "Candidate not found");
            candidates.delete(id);
            ctx.status(204);
        };
    }

    public Handler linkSkill() {
        return ctx -> {
            int candidateId = Integer.parseInt(ctx.pathParam("candidateId"));
            int skillId = Integer.parseInt(ctx.pathParam("skillId"));
            CandidateDTO c = candidates.findById(candidateId);
            SkillDTO s = skills.findById(skillId);
            if (c == null || s == null) throw new ApiException(404, "Candidate or skill not found");
            candidates.linkSkill(candidateId, skillId);
            ctx.status(204);
        };
    }
}