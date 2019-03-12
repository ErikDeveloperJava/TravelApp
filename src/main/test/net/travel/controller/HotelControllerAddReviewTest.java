package net.travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.travel.config.security.CurrentUser;
import net.travel.dto.ReviewAddDto;
import net.travel.form.ReviewForm;
import net.travel.model.Hotel;
import net.travel.model.Review;
import net.travel.model.User;
import net.travel.model.enums.UserType;
import net.travel.repository.ReviewRepository;
import net.travel.service.*;
import net.travel.service.impl.ReviewServiceImpl;
import net.travel.util.NumberUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HotelController.class)
public class HotelControllerAddReviewTest {

    private static final String URL = "/hotel/review";
    private static final String SECURITY_SESSION_NAME = "SPRING_SECURITY_CONTEXT";

    @Autowired
    private MockMvc mvc;

    @MockBean
    @Qualifier("UDS")
    private UserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserOrderService userOrderService;

    @MockBean
    private WishListService wishListService;

    @MockBean
    private HotelService hotelService;

    @MockBean
    private NumberUtil numberUtil;

    @MockBean
    private ReviewService reviewService;

    private User user;

    private SecurityContext securityContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void before() {
        user = User
                .builder()
                .id(1)
                .email("email")
                .password("pass")
                .userType(UserType.USER)
                .registerDate(new Date())
                .build();
        CurrentUser currentUser = new CurrentUser(user, true);
        securityContext =
                new SecurityContextImpl(new
                        UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities()));
    }

    @Test
    public void testUnauthorizedUser() throws Exception {
        mvc
                .perform(post(URL))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testSendInvalidMessage() throws Exception {
        String requestBody = objectMapper
                .writeValueAsString(ReviewForm
                        .builder()
                        .rating(1)
                        .message("as")
                        .hotelId(2)
                        .build());
        String responseJson = objectMapper
                .writeValueAsString(ReviewAddDto
                        .builder()
                        .messageError(true)
                        .build());
        mvc.perform(post(URL)
                .sessionAttr(SECURITY_SESSION_NAME, securityContext)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseJson));
    }

    @Test
    public void testSendValidData() throws Exception {
        ReviewForm reviewForm = ReviewForm
                .builder()
                .rating(2)
                .message("message_01_01")
                .hotelId(2)
                .build();
        String requestBody = objectMapper
                .writeValueAsString(reviewForm);
        Review review = Review
                .builder()
                .sendDate(new Date())
                .message(reviewForm.getMessage())
                .rating(reviewForm.getRating())
                .hotel(Hotel
                        .builder()
                        .id(reviewForm.getHotelId())
                        .build())
                .user(user)
                .build();
        ReviewAddDto reviewAddDto = ReviewAddDto
                .builder()
                .review(ReviewServiceImpl.buildReviewDto(review))
                .messageError(false)
                .build();
        when(reviewService.add(review))
                .thenReturn(reviewAddDto);
        String responseJson = mvc.perform(post(URL)
                .sessionAttr(SECURITY_SESSION_NAME, securityContext)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();
        ReviewAddDto response = objectMapper.readValue(responseJson, ReviewAddDto.class);
        assertEquals(reviewAddDto,response);
    }
}