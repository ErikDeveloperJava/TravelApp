package net.travel.repository;

import net.travel.model.UserOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserOrderRepository extends JpaRepository<UserOrder,Integer> {

    int countByUser_id(int userId);

    List<UserOrder> findByUser_Id(int id, Pageable pageable);
}