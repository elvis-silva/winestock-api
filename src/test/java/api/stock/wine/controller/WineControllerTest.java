package api.stock.wine.controller;

import api.stock.wine.builder.WineDTOBuilder;
import api.stock.wine.dto.WineDTO;
import api.stock.wine.service.WineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static api.stock.wine.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
public class WineControllerTest {

    private static final String WINE_API_URL_PATH = "/api/v1/wines";
    private static final String WINE_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String WINE_API_SUBPATH_DECREMENT_URL = "/decrement";
    private static final long VALID_WINE_ID = 1L;
    private static final long INVALID_WINE_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private WineService wineService;

    @InjectMocks
    private WineController wineController;

    @BeforeEach
    void setTup() {
        mockMvc = MockMvcBuilders.standaloneSetup(wineController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAWineIsCreated() throws Exception {
        // GIVEN
        WineDTO wineDTO = WineDTOBuilder.builder().build().toWineDTO();

        // WHEN
        when(wineService.createWine(wineDTO)).thenReturn(wineDTO);

        // THEN
        mockMvc.perform(post(WINE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(wineDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(wineDTO.getName())))
                .andExpect(jsonPath("$.grape", is(wineDTO.getGrape())))
                .andExpect(jsonPath("$.type", is(wineDTO.getType().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // GIVEN
        WineDTO wineDTO = WineDTOBuilder.builder().build().toWineDTO();
        wineDTO.setGrape(null);

        // THEN
        mockMvc.perform(post(WINE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(wineDTO)))
                .andExpect(status().isBadRequest());
    }


}
