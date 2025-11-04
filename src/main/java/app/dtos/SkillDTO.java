package app.dtos;

import app.entities.enums.SkillCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillDTO {
    private Integer id;
    private String name;
    private String slug;
    private SkillCategory category;
    private String description;
    private Integer popularityScore;
    private Integer averageSalary;
}