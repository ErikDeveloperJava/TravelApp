package net.travel.service;

import net.travel.model.UserOrder;

import java.util.Optional;

public interface OrderService {

    void add(UserOrder order);

    int orderCountByUserId(int userId);

    Optional<UserOrder> getById(int id);

    void update(UserOrder userOrder);
}