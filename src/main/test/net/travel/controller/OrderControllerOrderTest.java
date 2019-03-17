package net.travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.travel.config.security.CurrentUser;
import net.travel.dto.OrderDto;
import net.travel.form.OrderForm;
import net.travel.model.Hotel;
import net.travel.model.HotelRoom;
import net.travel.model.User;
import net.travel.model.UserOrder;
import net.travel.model.enums.UserType;
import net.travel.service.HotelRoomService;
import net.travel.service.HotelService;
import net.travel.service.OrderService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.mock.web.MockRequestDispatcher.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = OrderController.class)
public class OrderControllerOrderTest {

    private static final String URL = "/order";
    private static final String SECURITY_SESSION_NAME = "SPRING_SECURITY_CONTEXT";

    private static SecurityContext securityContext;

    @Autowired
    private MockMvc mvc;

    private static User currentUser;

    @MockBean
    private HotelRoomService hotelRoomService;

    @MockBean
    private HotelService hotelService;

    @MockBean
    private OrderService orderService;

    @MockBean
    @Qualifier("UDS")
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private static SimpleDateFormat simpleDateFormat;

    @BeforeClass
    public static void beforeClass() {
        currentUser = User
                .builder()
                .id(1)
                .userType(UserType.USER)
                .email("email")
                .password("pass")
                .build();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(new CurrentUser(currentUser, true), null,
                AuthorityUtils.createAuthorityList(UserType.USER.name()));
        securityContext = new SecurityContextImpl(authenticationToken);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void unauthenticatedUserTest() throws Exception {
        mvc.perform(post(URL))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void invalidFormRequestTest() throws Exception {
        String requestContent = objectMapper
                .writeValueAsString(new OrderForm());

        String responseContent = mvc.perform(post(URL)
                .sessionAttr(SECURITY_SESSION_NAME, securityContext)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();
        OrderDto actualOrderDto =
                objectMapper.readValue(responseContent,OrderDto.class);
        List<String> expectedErrorList = Arrays
                .asList("whenDate","daysCount","hotelId","hotelRoomId");
        assertFalse(actualOrderDto.isSuccess());
        for (String errorName : actualOrderDto.getErrorList()) {
            assertTrue(expectedErrorList.contains(errorName));
        }
    }

    @Test
    public void invalidAdultAndChildrenRequestTest() throws Exception {
        String requestContent = objectMapper
                .writeValueAsString(OrderForm
                        .builder()
                        .whenDate(simpleDateFormat.parse("2018-01-01 22:22:22"))
                        .daysCount(1)
                        .hotelId(2)
                        .hotelRoomId(2)
                        .build());

        String responseContent = mvc.perform(post(URL)
                .sessionAttr(SECURITY_SESSION_NAME, securityContext)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();
        OrderDto actualOrderDto =
                objectMapper.readValue(responseContent,OrderDto.class);
        List<String> expectedErrorList = Arrays
                .asList("adultCount","childrenCount");
        assertFalse(actualOrderDto.isSuccess());
        assertEquals(expectedErrorList,actualOrderDto.getErrorList());
    }

    @Test
    public void invalidHotelIdTest() throws Exception {
        int hotelId = 10;
        String requestContent = objectMapper
                .writeValueAsString(OrderForm
                        .builder()
                        .whenDate(simpleDateFormat.parse("2018-01-01 22:22:22"))
                        .adultCount(1)
                        .daysCount(1)
                        .hotelId(hotelId)
                        .hotelRoomId(2)
                        .build());
        when(hotelService.existsById(hotelId)).thenReturn(false);
        String responseContent = mvc.perform(post(URL)
                .sessionAttr(SECURITY_SESSION_NAME, securityContext)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();
        OrderDto actualOrderDto =
                objectMapper.readValue(responseContent,OrderDto.class);
        List<String> expectedErrorList = Collections.singletonList("hotelId");
        assertFalse(actualOrderDto.isSuccess());
        assertEquals(expectedErrorList,actualOrderDto.getErrorList());
    }


    @Test
    public void invalidHotelRoomIdTest() throws Exception {
        int hotelRoomId = 10;
        String requestContent = objectMapper
                .writeValueAsString(OrderForm
                        .builder()
                        .whenDate(simpleDateFormat.parse("2018-01-01 22:22:22"))
                        .adultCount(1)
                        .daysCount(1)
                        .hotelRoomId(hotelRoomId)
                        .hotelId(2)
                        .build());
        when(hotelService.existsById(2)).thenReturn(true);
        when(hotelRoomService.getById(hotelRoomId,false)).thenReturn(Optional.empty());
        String responseContent = mvc.perform(post(URL)
                .sessionAttr(SECURITY_SESSION_NAME, securityContext)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();
        OrderDto actualOrderDto =
                objectMapper.readValue(responseContent,OrderDto.class);
        List<String> expectedErrorList = Collections.singletonList("hotelRoomId");
        assertFalse(actualOrderDto.isSuccess());
        assertEquals(expectedErrorList,actualOrderDto.getErrorList());
    }

    @Test
    public void allHotelRoomsAreBusiedTest() throws Exception {
        int hotelRoomId = 10;
        String requestContent = objectMapper
                .writeValueAsString(OrderForm
                        .builder()
                        .whenDate(simpleDateFormat.parse("2018-01-01 22:22:22"))
                        .adultCount(1)
                        .daysCount(1)
                        .hotelRoomId(hotelRoomId)
                        .hotelId(2)
                        .build());
        when(hotelService.existsById(2)).thenReturn(true);
        when(hotelRoomService.getById(hotelRoomId,false)).thenReturn(Optional.of(HotelRoom
                .builder()
                .id(hotelRoomId)
                .busiedCount(12)
                .count(12)
                .build()));
        String responseContent = mvc.perform(post(URL)
                .sessionAttr(SECURITY_SESSION_NAME, securityContext)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();
        OrderDto actualOrderDto =
                objectMapper.readValue(responseContent,OrderDto.class);
        List<String> expectedErrorList = Collections.singletonList("allRoomsBusied");
        assertFalse(actualOrderDto.isSuccess());
        assertEquals(expectedErrorList,actualOrderDto.getErrorList());
    }

    @Test
    public void everythingOkTest() throws Exception {
        int hotelRoomId = 10;
        double price = 200.12;
        int daysCount = 4;
        OrderForm orderForm = OrderForm
                .builder()
                .whenDate(simpleDateFormat.parse("2018-01-01 22:22:22"))
                .adultCount(1)
                .daysCount(daysCount)
                .hotelRoomId(hotelRoomId)
                .hotelId(2)
                .build();
        String requestContent = objectMapper
                .writeValueAsString(orderForm);
        when(hotelService.existsById(2)).thenReturn(true);
        HotelRoom hotelRoom = HotelRoom
                .builder()
                .id(hotelRoomId)
                .busiedCount(10)
                .count(12)
                .price(price)
                .build();
        when(hotelRoomService.getById(hotelRoomId,false)).thenReturn(Optional.of(hotelRoom));
        String responseContent = mvc.perform(post(URL)
                .sessionAttr(SECURITY_SESSION_NAME, securityContext)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();
        OrderDto actualOrderDto =
                objectMapper.readValue(responseContent,OrderDto.class);
        hotelRoom.setBusiedCount(hotelRoom.getBusiedCount()  + 1);
        UserOrder userOrder = UserOrder
                .builder()
                .whenDate(orderForm.getWhenDate())
                .howManyDays(orderForm.getDaysCount())
                .price(price * daysCount)
                .adultCount(orderForm.getAdultCount())
                .orderDate(new Date())
                .childrenCount(orderForm.getChildrenCount())
                .hotel(Hotel
                        .builder()
                        .id(orderForm.getHotelId())
                        .build())
                .hotelRoom(hotelRoom)
                .user(currentUser)
                .build();
        assertTrue(actualOrderDto.isSuccess());
        assertTrue(actualOrderDto.getErrorList().isEmpty());
    }
}