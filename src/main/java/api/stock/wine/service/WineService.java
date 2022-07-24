package api.stock.wine.service;

import api.stock.wine.dto.WineDTO;
import api.stock.wine.entity.Wine;
import api.stock.wine.exception.WineAlreadyRegisteredException;
import api.stock.wine.exception.WineNotFoundException;
import api.stock.wine.exception.WineStockExceededException;
import api.stock.wine.mapper.WineMapper;
import api.stock.wine.repository.WineRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WineService {

    private final WineRepository wineRepository;
    private final WineMapper wineMapper = WineMapper.INSTANCE;

    public WineDTO createWine(WineDTO wineDTO) throws WineAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(wineDTO.getName());
        Wine wine = wineMapper.toModel(wineDTO);
        Wine savedWine = wineRepository.save(wine);
        return wineMapper.toDTO(savedWine);
    }

    public WineDTO findByName(String name) throws WineNotFoundException {
        Wine wine = wineRepository.findByName(name).orElseThrow(() -> new WineNotFoundException(name));
        return wineMapper.toDTO(wine);
    }

    public List<WineDTO> listAll() {
        return wineRepository.findAll()
                .stream()
                .map(wineMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws WineNotFoundException {
        verifyIfExists(id);
        wineRepository.deleteById(id);
    }

    public WineDTO increment(Long id, int quantityToIncrement) throws WineNotFoundException, WineStockExceededException {
        Wine wineToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + wineToIncrementStock.getQuantity();
        if(quantityAfterIncrement <= wineToIncrementStock.getMax()) {
            wineToIncrementStock.setQuantity(quantityAfterIncrement);
            Wine incrementedWineStock = wineRepository.save(wineToIncrementStock);
            return wineMapper.toDTO(incrementedWineStock);
        }
        throw new WineStockExceededException(id, quantityToIncrement);
    }

    private Wine verifyIfExists(Long id) throws WineNotFoundException {
        return wineRepository.findById(id).orElseThrow(() -> new WineNotFoundException(id));
    }

    private void verifyIfIsAlreadyRegistered(String name) throws WineAlreadyRegisteredException {
        Optional<Wine> optionalWine = wineRepository.findByName(name);
        if(optionalWine.isPresent()) {
            throw new WineAlreadyRegisteredException(name);
        }
    }
}
