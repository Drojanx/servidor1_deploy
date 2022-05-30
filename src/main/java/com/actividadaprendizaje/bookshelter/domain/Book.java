package com.actividadaprendizaje.bookshelter.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String author;
    @Column
    private String category;
    @Column
    private float price;
    @Column(name = "creation_date")
    private LocalDate creationDate;
    @Column
    public float getAvgReview(){
        float sumReviews = 0;
        int numOfPublished = 0;
        for (Review review : reviews){
            if(review.isPublished())
                numOfPublished++;
                sumReviews += review.getStars();
        }
        return sumReviews/numOfPublished;
    }

    @Override
    public String toString() {
        return name + '-' + author;
    }

    @OneToMany(mappedBy = "book")
    private List<Purchase> purchases;
    @OneToMany(mappedBy = "book")
    private List<Review> reviews;
}
