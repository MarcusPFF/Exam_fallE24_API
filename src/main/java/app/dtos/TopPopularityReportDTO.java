package app.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//US-6
public class TopPopularityReportDTO {
    private Integer candidateId;
    private Double averagePopularity;
}