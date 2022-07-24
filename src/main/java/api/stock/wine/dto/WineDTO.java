package api.stock.wine.dto;

import api.stock.wine.enums.WineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WineDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private WineType type;

    @NotNull
    @Size(min = 1, max = 200)
    private String grape;

    @NotNull
    @Max(500)
    private Integer max;

    @NotNull
    @Max(100)
    private Integer quantity;
}
