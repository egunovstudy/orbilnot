package com.gegunov.foodstuff.jpa.repository;

import com.gegunov.foodstuff.jpa.model.Reservation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    @Transactional
    @Modifying
    @Query("update Reservation r set r.status = ?1 where r.orderNumber = ?2 and r.status = ?3")
    void updateStatusByOrderNumberAndStatus(Reservation.Status newStatus, String orderNumber,
            Reservation.Status oldStatus);

    List<Reservation> findByOrderNumberAndStatus(String orderNumber, Reservation.Status status);

}
