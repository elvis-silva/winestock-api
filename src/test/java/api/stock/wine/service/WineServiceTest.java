package api.stock.wine.service;

import api.stock.wine.builder.WineDTOBuilder;
import api.stock.wine.dto.WineDTO;
import api.stock.wine.entity.Wine;
import api.stock.wine.exception.WineAlreadyRegisteredException;
import api.stock.wine.exception.WineNotFoundException;
import api.stock.wine.exception.WineStockExceededException;
import api.stock.wine.mapper.WineMapper;
import api.stock.wine.repository.WineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Console;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WineServiceTest {

    private static final long INVALID_WINE_ID = 1L;

    @Mock
    private WineRepository wineRepository;

    private final WineMapper wineMapper = WineMapper.INSTANCE;

    @InjectMocks
    private WineService wineService;

    @Test
    void whenWineInformedThenItShouldBeCreated() throws WineAlreadyRegisteredException {

        // GIVEN
        WineDTO expectedWineDTO = WineDTOBuilder.builder().build().toWineDTO();
        Wine expectedSavedWine = wineMapper.toModel(expectedWineDTO);

        // WHEN
        when(wineRepository.findByName(expectedWineDTO.getName())).thenReturn(Optional.empty());
        when(wineRepository.save(expectedSavedWine)).thenReturn(expectedSavedWine);

        // THEN
        WineDTO createdWineDTO = wineService.createWine(expectedWineDTO);

        assertThat(createdWineDTO.getId(), is(equalTo(expectedWineDTO.getId())));
        assertThat(createdWineDTO.getName(), is(equalTo(expectedWineDTO.getName())));
        assertThat(createdWineDTO.getQuantity(), is(equalTo(expectedWineDTO.getQuantity())));
    }

    @Test
    void whenAlreadyRegisteredWineInformedThenAnExceptionShouldBeThrow() {
        // GIVEN
        WineDTO expectedWineDTO = WineDTOBuilder.builder().build().toWineDTO();
        Wine duplicatedWine = wineMapper.toModel(expectedWineDTO);

        // WHEN
        when(wineRepository.findByName(expectedWineDTO.getName())).thenReturn(Optional.of(duplicatedWine));

        // THEN
        assertThrows(WineAlreadyRegisteredException.class, () -> wineService.createWine(expectedWineDTO));
    }

    @Test
    void whenValidWineNameIsGivenThenReturnAWine() throws WineNotFoundException {
        // GIVEN
        WineDTO expectedFoundWineDTO = WineDTOBuilder.builder().build().toWineDTO();
        Wine expectedFoundWine = wineMapper.toModel(expectedFoundWineDTO);

        // WHEN
        when(wineRepository.findByName(expectedFoundWine.getName())).thenReturn(Optional.of(expectedFoundWine));

        // THEN
        WineDTO foundWineDTO = wineService.findByName(expectedFoundWineDTO.getName());
        assertThat(foundWineDTO, is(equalTo(expectedFoundWineDTO)));
    }

    @Test
    void whenNotRegisteredWineNameIsGivenThenThrowAnException() {
        // GIVEN
        WineDTO expectedFoundWineDTO = WineDTOBuilder.builder().build().toWineDTO();

        // WHEN
        when(wineRepository.findByName(expectedFoundWineDTO.getName())).thenReturn(Optional.empty());

        // THEN
        assertThrows(WineNotFoundException.class, () -> wineService.findByName(expectedFoundWineDTO.getName()));
    }

    @Test
    void whenListWineIsCalledThenReturnAListOfWines() {
        // GIVEN
        WineDTO expectedFoundWineDTO = WineDTOBuilder.builder().build().toWineDTO();
        Wine expectedFoundWine = wineMapper.toModel(expectedFoundWineDTO);

        // WHEN
        when(wineRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundWine));

        // THEN
        List<WineDTO> foundListWinesDTO = wineService.listAll();

        assertThat(foundListWinesDTO, is(not(empty())));
        assertThat(foundListWinesDTO.get(0), is(equalTo(expectedFoundWineDTO)));
    }

    @Test
    void whenListWineIsCalledThenReturnAnEmptyListOfWines() {
        // WHEN
        when(wineRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        // THEN
        List<WineDTO> foundListWinesDTO = wineService.listAll();

        assertThat(foundListWinesDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAWineShouldBeDeleted() throws WineNotFoundException {
        // GIVEN
        WineDTO expectedDeletedWineDTO = WineDTOBuilder.builder().build().toWineDTO();
        Wine expectedDeletedWine = wineMapper.toModel(expectedDeletedWineDTO);

        // WHEN
        when(wineRepository.findById(expectedDeletedWineDTO.getId())).thenReturn(Optional.of(expectedDeletedWine));
        doNothing().when(wineRepository).deleteById(expectedDeletedWineDTO.getId());

        // THEN
        wineService.deleteById(expectedDeletedWineDTO.getId());

        verify(wineRepository, times(1)).findById(expectedDeletedWineDTO.getId());
        verify(wineRepository, times(1)).deleteById(expectedDeletedWineDTO.getId());
    }

    @Test
    void whenIncrementIsCalledThenIncrementWineStock() throws WineNotFoundException, WineStockExceededException {
        // GIVEN
        WineDTO expectedWineDTO = WineDTOBuilder.builder().build().toWineDTO();
        Wine expectedWine = wineMapper.toModel(expectedWineDTO);

        // WHEN
        when(wineRepository.findById(expectedWineDTO.getId())).thenReturn(Optional.of(expectedWine));
        when(wineRepository.save(expectedWine)).thenReturn(expectedWine);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedWineDTO.getQuantity() + quantityToIncrement;

        System.out.println(expectedQuantityAfterIncrement);

        // THEN
        WineDTO incrementedWineDTO = wineService.increment(expectedWineDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedWineDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedWineDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreaterThanMaxThenThrowException() {
        // GIVEN
        WineDTO expectedWineDTO = WineDTOBuilder.builder().build().toWineDTO();
        Wine expectedWine = wineMapper.toModel(expectedWineDTO);

        // WHEN
        when(wineRepository.findById(expectedWineDTO.getId())).thenReturn(Optional.of(expectedWine));

        // THEN
        int quantityToIncrement = 80;
        assertThrows(WineStockExceededException.class, () -> wineService.increment(expectedWineDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreaterThanMaxThenThrowException() {
        // GIVEN
        WineDTO expectedWineDTO = WineDTOBuilder.builder().build().toWineDTO();
        Wine expectedWine = wineMapper.toModel(expectedWineDTO);

        // WHEN
        when(wineRepository.findById(expectedWineDTO.getId())).thenReturn(Optional.of(expectedWine));

        // THEN
        int quantityToIncrement = 100;
        assertThrows(WineStockExceededException.class, () -> wineService.increment(expectedWine.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        // WHEN
        int quantityToIncrement = 10;

        when(wineRepository.findById(INVALID_WINE_ID)).thenReturn(Optional.empty());

        // THEN
        assertThrows(WineNotFoundException.class, () -> wineService.increment(INVALID_WINE_ID, quantityToIncrement));
    }
}
