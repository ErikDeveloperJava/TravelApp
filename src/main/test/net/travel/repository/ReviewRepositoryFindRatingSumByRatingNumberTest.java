package net.travel.repository;

import net.travel.model.*;
import net.travel.model.enums.UserStatus;
import net.travel.model.enums.UserType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:data_test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRepositoryFindRatingSumByRatingNumberTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ContactRepository contactRepository;

    private User user;

    private Hotel hotel;

    @Before
    public void before() {
        Region region = Region
                .builder()
                .nameArm("")
                .nameRu("")
                .nameEn("")
                .build();
        regionRepository.save(region);
        City city = City
                .builder()
                .nameArm("")
                .nameRu("")
                .nameEn("")
                .region(region)
                .build();
        cityRepository.save(city);
        user = User
                .builder()
                .imgUrl("")
                .contact(Contact
                        .builder()
                        .phone("")
                        .address("")
                        .city(city)
                        .region(region)
                        .build())
                .userType(UserType.USER)
                .password("")
                .email("")
                .userStatus(UserStatus.ACTIVE)
                .name("")
                .registerDate(new Date())
                .surname("")
                .build();
        userRepository.save(user);
        hotel = Hotel
                .builder()
                .imgUrl("")
                .contact(Contact
                        .builder()
                        .phone("")
                        .address("")
                        .city(city)
                        .region(region)
                        .build())
                .information("")
                .name("")
                .build();
        hotelRepository.save(hotel);
    }

    @Test
    public void testRatingSumByRatingNumber() {
        List<Review> reviewList = Arrays
                .asList(Review
                                .builder()
                                .user(user)
                                .sendDate(new Date())
                                .hotel(hotel)
                                .rating(5)
                                .message("")
                                .build(),
                        Review
                                .builder()
                                .user(user)
                                .sendDate(new Date())
                                .hotel(hotel)
                                .rating(5)
                                .message("")
                                .build(),
                        Review
                                .builder()
                                .user(user)
                                .sendDate(new Date())
                                .hotel(hotel)
                                .rating(2)
                                .message("")
                                .build(),
                        Review
                                .builder()
                                .user(user)
                                .sendDate(new Date())
                                .hotel(hotel)
                                .rating(3)
                                .message("")
                                .build(),
                        Review
                                .builder()
                                .user(user)
                                .sendDate(new Date())
                                .hotel(hotel)
                                .rating(3)
                                .message("")
                                .build(),
                        Review
                                .builder()
                                .user(user)
                                .sendDate(new Date())
                                .hotel(hotel)
                                .rating(2)
                                .message("")
                                .build());
        reviewRepository.saveAll(reviewList);
        List<Integer> ratingNumberList = Arrays.asList(10,null,6,4,null);
        for (int i = 5,j=0; i >0 ; i--,j++) {
            Integer result = reviewRepository.findSumHotelRatingByRatingNumber(i, hotel.getId());
            assertEquals(ratingNumberList.get(j),result);
        }
    }
}