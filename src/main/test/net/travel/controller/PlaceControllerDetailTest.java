package net.travel.controller;

import net.travel.model.Contact;
import net.travel.model.Place;
import net.travel.model.PlaceImage;
import net.travel.service.PlaceService;
import net.travel.service.UserOrderService;
import net.travel.service.UserService;
import net.travel.service.WishListService;
import net.travel.util.AuthenticationUtil;
import net.travel.util.DataUtil;
import net.travel.util.NumberUtil;
import net.travel.util.model.TourDetailData;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PlaceController.class)
@ContextConfiguration(classes = PlaceControllerDetailTest.Config.class)
public class PlaceControllerDetailTest {

    private static final String URI = "/place/detail/";

    @Autowired
    private MockMvc mvc;

    @MockBean
    @Qualifier("UDS")
    private UserDetailsService userDetailsService;

    @MockBean
    private PlaceService placeService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserOrderService userOrderService;

    @MockBean
    private WishListService wishListService;

    @Autowired
    private NumberUtil numberUtil;

    @MockBean
    private AuthenticationUtil authenticationUtil;

    @Autowired
    private DataUtil dataUtil;

    @TestConfiguration
    static class Config{

        @Bean
        public NumberUtil numberUtil(){
            return new NumberUtil();
        }

        @Bean
        public DataUtil dataUtil(){
            return new DataUtil();
        }
    }

    @Test
    public void testStrId()throws Exception{
        mvc.perform(get(URI + "abc"))
                .andDo(print())
                .andExpect(redirectedUrl("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testWrongPlaceId()throws Exception{
        int placeId = 2;
        when(placeService.existsById(placeId)).thenReturn(false);
        mvc.perform(get(URI + placeId))
                .andDo(print())
                .andExpect(redirectedUrl("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testEveryThingOk()throws Exception{
        int placeId = 2;
        when(placeService.existsById(placeId)).thenReturn(true);
        TourDetailData<Place, PlaceImage> expectTourDetail = TourDetailData
                .<Place, PlaceImage>builder()
                .ratingPercentList(new ArrayList<>())
                .reviewList(new ArrayList<>())
                .imageList(new ArrayList<>())
                .model(Place
                        .builder()
                        .imgUrl("image")
                        .hotelList(new ArrayList<>())
                        .contact(Contact
                                .builder()
                                .build())
                        .build())
                .rating(12)
                .build();
        when(placeService.getDetailById(placeId,-1))
                .thenReturn(expectTourDetail);
        mvc.perform(get(URI + placeId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("tourDetail",expectTourDetail));
    }
}