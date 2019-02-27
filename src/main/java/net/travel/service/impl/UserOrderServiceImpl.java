package net.travel.service.impl;

import net.travel.repository.UserOrderRepository;
import net.travel.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserOrderServiceImpl implements UserOrderService {

    @Autowired
    private UserOrderRepository userOrderRepository;

    @Override
    public int countByUserId(int userId) {
        return userOrderRepository.countByUser_id(userId);
    }
}
