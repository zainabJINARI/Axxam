package com.example.demo.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class DetailedAnnouncementRating {
    private String id;
    private String title;
    private String description;
    private String address;
    private double priceForNight;
    private List<String> imageUrl; // One image URL from the list of images
    private double averageRating;

    public DetailedAnnouncementRating(String id, String title, String description, String address, double priceForNight, String imageUrl, double averageRating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.priceForNight = priceForNight;
        this.imageUrl = List.of(imageUrl);
        this.averageRating = averageRating;
    }

    // Getters and setters
}
