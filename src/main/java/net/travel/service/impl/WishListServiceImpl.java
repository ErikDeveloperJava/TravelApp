package net.travel.service.impl;

import net.travel.dto.WishListAddDto;
import net.travel.model.Hotel;
import net.travel.model.Place;
import net.travel.model.User;
import net.travel.model.WishList;
import net.travel.repository.WishListRepository;
import net.travel.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class WishListServiceImpl implements WishListService {

    @Autowired
    private WishListRepository wishListRepository;

    @Override
    public int countByUserId(int userId) {
        return wishListRepository.countByUser_id(userId);
    }

    @Transactional
    public boolean add(int userId, WishListAddDto wishListAddDto) {
        if(wishListAddDto.getModelType().equals("place")){
            Optional<WishList> optionalWishList = wishListRepository.findByUser_idAndPlace_id(userId, wishListAddDto.getModelId());
            if(optionalWishList.isPresent()){
                wishListRepository.delete(optionalWishList.get());
                return false;
            }else {
                WishList wishList = WishList
                        .builder()
                        .user(User
                                .builder()
                                .id(userId)
                                .build())
                        .place(Place
                                .builder()
                                .id(wishListAddDto.getModelId())
                                .build())
                        .build();
                wishListRepository.save(wishList);
                return true;
            }
        }else {
            Optional<WishList> optionalWishList = wishListRepository.findByUser_idAndHotel_id(userId, wishListAddDto.getModelId());
            if(optionalWishList.isPresent()){
                wishListRepository.delete(optionalWishList.get());
                return false;
            }else {
                WishList wishList = WishList
                        .builder()
                        .user(User
                                .builder()
                                .id(userId)
                                .build())
                        .hotel(Hotel
                                .builder()
                                .id(wishListAddDto.getModelId())
                                .build())
                        .build();
                wishListRepository.save(wishList);
                return true;
            }

        }
    }
}