package ma.axxam.reservation.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ma.axxam.reservation.entities.Reservation;
import ma.axxam.reservation.enums.ReservationStatus;



@RepositoryRestResource
public interface ReservationRepository extends JpaRepository<Reservation,String>{
	Page findAll(Pageable pageable);
    Page<Reservation> findByUserId(String userId, Pageable pageable);
    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);
	Page<Reservation> findByPropertyId(String propertyId, Pageable pageable);

}
