package app.services;

import app.daos.CandidateDAO;
import app.dtos.TopPopularityReportDTO;
import app.entities.Candidate;
import app.entities.CandidateSkill;
import app.entities.Skill;
import app.services.FetchSlugsFromApi.SkillData;
import app.services.FetchSlugsFromApi.SkillStatsResponse;
import app.services.fetchtools.FetchTools;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ReportService {
    private final CandidateDAO dao;

    public ReportService(EntityManagerFactory emf) {
        this.dao = new CandidateDAO(emf);
    }

    public TopPopularityReportDTO topByPopularity() {
        List<Candidate> candidates = dao.getAll();
        SkillStatsResponse res = new FetchSlugsFromApi(new FetchTools()).FetchSlugsFromUrl();
        List<SkillData> data = res == null ? null : res.data;

        Integer bestId = null;
        double bestAvg = 0.0;

        for (Candidate c : candidates) {
            double sum = 0.0;
            int count = 0;

            if (c.getCandidateSkills() != null && data != null) {
                for (CandidateSkill cs : c.getCandidateSkills()) {
                    Skill s = cs.getSkill();
                    String slug = s == null ? null : s.getSlug();
                    if (slug == null || slug.isBlank()) continue;
                    String key = slug.toLowerCase();

                    for (SkillData d : data) {
                        if (d.slug != null && key.equals(d.slug.toLowerCase()) && d.popularityScore != null) {
                            sum += d.popularityScore;
                            count++;
                            break;
                        }
                    }
                }
            }

            double avg = count == 0 ? 0.0 : sum / count;
            if (bestId == null || avg > bestAvg) {
                bestId = c.getId();
                bestAvg = avg;
            }
        }
        return new TopPopularityReportDTO(bestId, bestAvg);
    }
}