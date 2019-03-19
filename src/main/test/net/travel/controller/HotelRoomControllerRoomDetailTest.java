package net.travel.controller;

import net.travel.model.Hotel;
import net.travel.model.HotelRoom;
import net.travel.model.HotelRoomAttribute;
import net.travel.service.HotelRoomService;
import net.travel.service.UserOrderService;
import net.travel.service.UserService;
import net.travel.service.WishListService;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.mock.web.MockRequestDispatcher.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HotelRoomController.class)
@ContextConfiguration(classes = HotelRoomControllerRoomDetailTest.Config.class)
public class HotelRoomControllerRoomDetailTest {

    private static final String URI = "/hotel/room/detail/";

    @Autowired
    private MockMvc mvc;

    @MockBean
    @Qualifier("UDS")
    private UserDetailsService userDetailsService;

    @MockBean
    private HotelRoomService hotelRoomService;

    @Autowired
    private NumberUtil numberUtil;

    @MockBean
    private UserService userService;

    @MockBean
    private UserOrderService userOrderService;

    @MockBean
    private WishListService wishListService;

    @MockBean
    private AuthenticationUtil authenticationUtil;

    @TestConfiguration
    static class Config{
        @Bean
        public NumberUtil numberUtil(){
            return new NumberUtil();
        }
    }

    @Test
    public void testStrId()throws Exception{
        mvc.perform(get(URI + "abc"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testNotExistsId()throws Exception{
        int id = 2;
        when(hotelRoomService.existsById(id))
                .thenReturn(false);
        mvc.perform(get(URI + id))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testOkParameter()throws Exception{
        int id = 2;
        when(hotelRoomService.existsById(id))
                .thenReturn(true);
        HotelRoom hotelRoom = HotelRoom
                .builder()
                .id(id)
                .imageList(new ArrayList<>())
                .hotel(Hotel.builder().build())
                .hotelRoomAttribute(HotelRoomAttribute.builder().build())
                .build();
        when(hotelRoomService.getById(id,true))
                .thenReturn(Optional.of(hotelRoom));
        mvc.perform(get(URI + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("hotelRoom",hotelRoom));
    }
}