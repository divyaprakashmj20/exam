package edu.tus.winemanager.repository;

import edu.tus.winemanager.entity.Wine;
import edu.tus.winemanager.enums.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WineRepository extends JpaRepository<Wine, Long> {
    Page<Wine> findAll(Pageable pageable);

    Optional<List<Wine>> findByRating(Rating rating);

    Optional<List<Wine>> findByExpiryDate(LocalDate expiry_date);

    Optional<List<Wine>> findByExpiryDateLessThanEqual(LocalDate expiry_date);

    Optional<List<Wine>> findByRatingAndExpiryDate(Rating rating, LocalDate expiry_date);

    Optional<List<Wine>> findByRatingAndExpiryDateLessThanEqual(Rating rating, LocalDate expiry_date);
}
