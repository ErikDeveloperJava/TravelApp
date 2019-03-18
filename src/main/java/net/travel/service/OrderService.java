package net.travel.service;

import net.travel.model.UserOrder;

public interface OrderService {

    void add(UserOrder order);

    int orderCountByUserId(int userId);
}
