package api.stock.wine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WineType {

    RED("Red"),
    WHITE("White"),
    GREEN("green"),
    ROSE("Rose"),
    LIGHT("light"),
    SWEET("Sweet"),
    DRY("Dry"),
    SPARKLING("Sparkling");

    private final String description;
}
