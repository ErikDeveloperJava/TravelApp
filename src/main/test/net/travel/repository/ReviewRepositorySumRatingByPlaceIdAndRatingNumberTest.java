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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:data_test.properties")
public class ReviewRepositorySumRatingByPlaceIdAndRatingNumberTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CityRepository cityRepository;

    private User user;

    private Place place;

    @Before
    public void before() {
        Region region = Region
                .builder()
                .nameArm("asd")
                .nameEn("asd")
                .nameRu("asd")
                .build();
        regionRepository.save(region);
        City city = City
                .builder()
                .region(region)
                .nameArm("asd")
                .nameRu("asd")
                .nameEn("asd")
                .build();
        cityRepository.save(city);
        user = User
                .builder()
                .password("asdasd")
                .email("asdfetrghdg")
                .userType(UserType.USER)
                .registerDate(new Date())
                .surname("surname")
                .name("name")
                .userStatus(UserStatus.INACTIVE)
                .contact(Contact
                        .builder()
                        .region(region)
                        .city(city)
                        .address("asd")
                        .phone("asd")
                        .build())
                .imgUrl("asd")
                .build();
        userRepository.save(user);
        place = Place
                .builder()
                .contact(Contact
                        .builder()
                        .region(region)
                        .city(city)
                        .address("asd")
                        .phone("asd")
                        .build())
                .imgUrl("asd")
                .information("info")
                .name("name")
                .price(1232.32)
                .build();
        placeRepository.save(place);
    }

    @Test
    public void testRatingsSum() {
        List<Review> reviewList = Arrays
                .asList(Review
                                .builder()
                                .message("message")
                                .user(user)
                                .place(place)
                                .rating(5)
                                .sendDate(new Date())
                                .build(),
                        Review
                                .builder()
                                .message("message")
                                .user(user)
                                .place(place)
                                .rating(5)
                                .sendDate(new Date())
                                .build(),
                        Review
                                .builder()
                                .message("message")
                                .user(user)
                                .place(place)
                                .rating(2)
                                .sendDate(new Date())
                                .build());
        reviewRepository.saveAll(reviewList);
        List<Integer> expectRatingSumList = Arrays
                .asList(null,2,null,null,10);
        List<Integer> actualRatingSumList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            actualRatingSumList.add(reviewRepository
                    .findSumPlaceRatingByRatingNumber(i,place.getId()));
        }
        assertEquals(expectRatingSumList,actualRatingSumList);
    }
}