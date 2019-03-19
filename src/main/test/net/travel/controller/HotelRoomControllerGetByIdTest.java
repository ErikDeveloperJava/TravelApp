package net.travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.travel.dto.HotelRoomDto;
import net.travel.model.Hotel;
import net.travel.model.HotelRoom;
import net.travel.service.HotelRoomService;
import net.travel.service.impl.HotelRoomServiceImpl;
import net.travel.service.impl.ReviewServiceImpAddTest;
import net.travel.util.AuthenticationUtil;
import net.travel.util.NumberUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.thymeleaf.spring5.expression.Mvc;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HotelRoomController.class)
@ContextConfiguration(classes = HotelRoomControllerGetByIdTest.TestConfig.class)
public class HotelRoomControllerGetByIdTest {

    private static final String URL = "/hotel/room/id/";

    @Autowired
    private MockMvc mvc;

    @MockBean
    @Qualifier("UDS")
    private UserDetailsService userDetailsService;

    @MockBean
    private HotelRoomService hotelRoomService;

    @Autowired
    private NumberUtil numberUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationUtil authenticationUtil;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public NumberUtil numberUtil() {
            return new NumberUtil();
        }
    }

    @Test
    public void testStrId() throws Exception {
        mvc.perform(get(URL + "abc"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWrongHotelRoomId() throws Exception {
        int hotelRoomId = 10;
        when(hotelRoomService.getById(hotelRoomId,false)).thenReturn(Optional.empty());
        mvc.perform(get(URL + hotelRoomId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNormalHotelRoomId() throws Exception {
        int hotelRoomId = 1;
        HotelRoom hotelRoom = HotelRoom
                .builder()
                .id(1)
                .hotel(Hotel
                        .builder()
                        .build())
                .build();
        HotelRoomDto expectedHotelRoom = HotelRoomServiceImpl.buildHotelRoomDto(hotelRoom);
        when(hotelRoomService.getById(hotelRoomId,false)).thenReturn(Optional.of(hotelRoom));
        mvc.perform(get(URL + hotelRoomId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedHotelRoom)));
    }
}