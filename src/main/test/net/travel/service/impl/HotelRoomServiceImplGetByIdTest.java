package net.travel.service.impl;

import net.travel.model.HotelRoom;
import net.travel.model.HotelRoomImage;
import net.travel.repository.HotelRoomRepository;
import net.travel.service.HotelRoomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = HotelRoomServiceImplGetByIdTest.Config.class)
public class HotelRoomServiceImplGetByIdTest {

    @MockBean
    private HotelRoomRepository hotelRoomRepository;

    @Autowired
    private HotelRoomService hotelRoomService;

    @TestConfiguration
    static class Config{

        @Bean
        public HotelRoomService hotelRoomService(){
            return new HotelRoomServiceImpl();
        }
    }

    @Test(expected = WantedButNotInvoked.class)
    public void testFalseParameter(){
        int id = 2;
        HotelRoom hotelRoom = mock(HotelRoom.class);
        when(hotelRoomRepository.findById(id))
                .thenReturn(Optional.of(hotelRoom));
        hotelRoomService.getById(id,false);
        verify(hotelRoom).getImageList();

    }

    @Test
    public void testTrueParameter(){
        int id = 2;
        HotelRoom hotelRoom = mock(HotelRoom.class);
        when(hotelRoomRepository.findById(id))
                .thenReturn(Optional.of(hotelRoom));
        hotelRoomService.getById(id,true);
        verify(hotelRoom).getImageList();
    }
}