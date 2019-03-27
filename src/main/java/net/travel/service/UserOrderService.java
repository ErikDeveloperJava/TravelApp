package net.travel.service;

import net.travel.model.UserOrder;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserOrderService {

    int countByUserId(int userId);

    int count();

    List<UserOrder> getAll(Pageable pageable);
}