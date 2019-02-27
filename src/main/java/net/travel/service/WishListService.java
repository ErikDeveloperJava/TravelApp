package net.travel.service;

import net.travel.dto.WishListAddDto;

public interface WishListService {

    int countByUserId(int userId);

    boolean add(int userId, WishListAddDto wishListAddDto);
}