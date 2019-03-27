package net.travel.service.impl;

import net.travel.model.UserOrder;
import net.travel.repository.UserOrderRepository;
import net.travel.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOrderServiceImpl implements UserOrderService {

    @Autowired
    private UserOrderRepository userOrderRepository;

    @Override
    public int countByUserId(int userId) {
        return userOrderRepository.countByUser_id(userId);
    }

    @Override
    public int count() {
        return (int) userOrderRepository.count();
    }

    @Override
    public List<UserOrder> getAll(Pageable pageable) {
        return userOrderRepository.findAll(pageable).getContent();
    }
}
