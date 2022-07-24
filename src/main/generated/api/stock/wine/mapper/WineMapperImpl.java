package api.stock.wine.mapper;

import api.stock.wine.dto.WineDTO;
import api.stock.wine.entity.Wine;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-24T12:21:33-0300",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.15 (Oracle Corporation)"
)
public class WineMapperImpl implements WineMapper {

    @Override
    public Wine toModel(WineDTO wineDTO) {
        if ( wineDTO == null ) {
            return null;
        }

        Wine wine = new Wine();

        wine.setId( wineDTO.getId() );
        wine.setName( wineDTO.getName() );
        wine.setType( wineDTO.getType() );
        wine.setGrape( wineDTO.getGrape() );
        if ( wineDTO.getMax() != null ) {
            wine.setMax( wineDTO.getMax() );
        }
        if ( wineDTO.getQuantity() != null ) {
            wine.setQuantity( wineDTO.getQuantity() );
        }

        return wine;
    }

    @Override
    public WineDTO toDTO(Wine wine) {
        if ( wine == null ) {
            return null;
        }

        WineDTO.WineDTOBuilder wineDTO = WineDTO.builder();

        wineDTO.id( wine.getId() );
        wineDTO.name( wine.getName() );
        wineDTO.type( wine.getType() );
        wineDTO.grape( wine.getGrape() );
        wineDTO.max( wine.getMax() );
        wineDTO.quantity( wine.getQuantity() );

        return wineDTO.build();
    }
}
