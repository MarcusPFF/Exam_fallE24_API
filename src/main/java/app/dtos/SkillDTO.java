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
    private SkillCategory category;
    private String description;
}