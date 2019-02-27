package net.travel.repository;

import net.travel.model.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrderRepository extends JpaRepository<UserOrder,Integer> {

    int countByUser_id(int userId);
}
