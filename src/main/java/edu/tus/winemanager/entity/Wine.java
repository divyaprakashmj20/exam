package edu.tus.winemanager.entity;

import edu.tus.winemanager.enums.Rating;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "WINE")
public class Wine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

//    @Pattern(regexp="^[A-Za-z]*$",message = "Invalid Input")
    String name;

    @Min(value = 1900,message = "Year must be greater than 1900")
    @Max(value = 3000,message = "Year must be lesser than 3000")
    Integer year;
    String grapes;
    String country;
    String region;
    @Enumerated(EnumType.STRING)
    Rating rating;

    @Column(name = "expiry_date")
    LocalDate expiryDate;
}
