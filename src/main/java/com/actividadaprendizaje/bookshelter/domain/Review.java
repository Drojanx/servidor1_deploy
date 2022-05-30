package com.actividadaprendizaje.bookshelter.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews",
        uniqueConstraints = { @UniqueConstraint(columnNames = //Un libro solo se puede reseñar una vez por usuario
                { "user_id", "book_id" }) })
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private float stars;
    @Column
    private String comment;
    @Column(name = "creation_date")
    private LocalDate creationDate;
    @Column
    private boolean published;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}
