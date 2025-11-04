package app.services;

import app.services.fetchtools.FetchTools;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

//US-5
public class FetchSlugsFromApi {
    private final FetchTools fetchTools;

    public FetchSlugsFromApi(FetchTools fetchTools) {
        this.fetchTools = fetchTools;
    }

    public SkillStatsResponse FetchSlugsFromUrl() {
        return fetchTools.getFromApi(
                "https://apiprovider.cphbusinessapps.dk/api/v1/skills/stats?slugs=" +
                        "java,postgresql,docker,react,junit,tensorflow,javalin",
                SkillStatsResponse.class
        );
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SkillStatsResponse {
        public List<SkillData> data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SkillData {
        public String slug;
        public Integer popularityScore;
        public Integer averageSalary;
    }
}