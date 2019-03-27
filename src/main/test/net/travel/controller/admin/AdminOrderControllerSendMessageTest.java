package net.travel.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.travel.dto.SendMessageDto;
import net.travel.form.SendMessage;
import net.travel.model.User;
import net.travel.model.UserOrder;
import net.travel.service.MailService;
import net.travel.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AdminOrderController.class)
public class AdminOrderControllerSendMessageTest {

    private static final String URI = "/admin/send-email-message";

    @Autowired
    private MockMvc mvc;

    @MockBean
    @Qualifier("UDS")
    private UserDetailsService userDetailsService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private MailService mailService;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(authorities = "USER",username = "user",password = "pass")
    @Test
    public void unAuthorizedUserTest()throws Exception{
        mvc.perform(post(URI).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @WithMockUser(authorities= "ADMIN",username = "admin",password = "pass")
    @Test
    public void invalidMessageAndSubjectTest()throws Exception{
        SendMessageDto expectedObject = SendMessageDto
                .builder()
                .errorList(Arrays.asList("message", "subject"))
                .build();
        String requestContent = objectMapper.writeValueAsString(SendMessage
                .builder()
                .subject("a")
                .message("aaa")
                .build());
        String actualContent = mvc.perform(post(URI)
                .content(requestContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()
                .getResponse()
                .getContentAsString();
        SendMessageDto actualObject = objectMapper.readValue(actualContent, SendMessageDto.class);
        assertTrue(actualObject.getErrorList().containsAll(expectedObject.getErrorList()));
        assertFalse(actualObject.isSuccess());
    }

    @WithMockUser(authorities= "ADMIN",username = "admin",password = "pass")
    @Test
    public void invalidOrderIdTest()throws Exception{
        int orderId = 12;
        String expectContent = objectMapper.writeValueAsString(SendMessageDto
                .builder()
                .errorList(Collections.singletonList("orderId"))
                .build());
        String requestContent = objectMapper.writeValueAsString(SendMessage
                .builder()
                .subject("subject")
                .message("message message")
                .orderId(orderId)
                .build());
        when(orderService.getById(orderId)).thenReturn(Optional.empty());
        String actualContent = mvc.perform(post(URI)
                .content(requestContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(expectContent,actualContent);
    }

    @WithMockUser(authorities = "ADMIN",username = "admin",password = "pass")
    @Test
    public void everythingOkTest()throws Exception{
        int orderId = 12;
        String expectContent = objectMapper.writeValueAsString(SendMessageDto
                .builder()
                .success(true)
                .errorList(new ArrayList<>())
                .build());
        SendMessage sendMessage = SendMessage
                .builder()
                .subject("subject")
                .message("message message")
                .orderId(orderId)
                .build();
        String requestContent = objectMapper.writeValueAsString(sendMessage);
        User user = User
                .builder()
                .email("user@gmail.com")
                .build();
        UserOrder userOrder = UserOrder
                .builder()
                .user(user)
                .build();
        when(orderService.getById(orderId)).thenReturn(Optional.of(userOrder));
        String actualContent = mvc.perform(post(URI)
                .content(requestContent)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(mailService).send(user.getEmail(),sendMessage.getMessage(),sendMessage.getSubject());
        userOrder.setSendMessage(true);
        verify(orderService).update(userOrder);
        assertEquals(expectContent,actualContent);
    }
}