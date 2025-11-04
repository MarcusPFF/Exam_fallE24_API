package app.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {
    private Integer id;
    private String name;
    private String phone;
    private String education;
    private List<SkillDTO> skills;
}