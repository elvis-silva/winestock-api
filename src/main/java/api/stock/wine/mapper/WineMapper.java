package api.stock.wine.mapper;

import api.stock.wine.dto.WineDTO;
import api.stock.wine.entity.Wine;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WineMapper {

    WineMapper INSTANCE = Mappers.getMapper(WineMapper.class);

    Wine toModel(WineDTO wineDTO);

    WineDTO toDTO(Wine wine);
}
