package api.stock.wine.builder;

import api.stock.wine.dto.WineDTO;
import api.stock.wine.enums.WineType;
import lombok.Builder;

@Builder
public class WineDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Casal Garcia";

    @Builder.Default
    private String grape = "Uva Verde";

    @Builder.Default
    private int max = 100;

    @Builder.Default
    private int quantity = 50;

    @Builder.Default
    private WineType type = WineType.GREEN;

    public WineDTO toWineDTO() {
        return new WineDTO(
                id,
                name,
                type,
                grape,
                max,
                quantity
        );
    }

}
