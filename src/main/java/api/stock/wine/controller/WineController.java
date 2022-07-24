package api.stock.wine.controller;

import api.stock.wine.dto.QuantityDTO;
import api.stock.wine.dto.WineDTO;
import api.stock.wine.exception.WineAlreadyRegisteredException;
import api.stock.wine.exception.WineNotFoundException;
import api.stock.wine.exception.WineStockExceededException;
import api.stock.wine.service.WineService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wines")
@AllArgsConstructor
public class WineController implements WineControllerDocs {

    private final WineService wineService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WineDTO createWine(WineDTO wineDTO) throws WineAlreadyRegisteredException {
        return wineService.createWine(wineDTO);
    }

    @GetMapping("/{name}")
    public WineDTO findByName(String name) throws WineNotFoundException {
        return wineService.findByName(name);
    }

    @GetMapping
    public List<WineDTO> listWines() {
        return wineService.listAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(Long id) throws WineNotFoundException {
        wineService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public WineDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws
            WineNotFoundException, WineStockExceededException {
        return wineService.increment(id, quantityDTO.getQuantity());
    }
}
