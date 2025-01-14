import { Component } from '@angular/core';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrl: './review.component.css'
})
export class ReviewComponent {
  public review = {
    user: {
      name: "Jane Doe",  // Reviewer's name
      accountCreationDate: "2019-08-12",  // Date when the user's account was created
      img: {
        src: "https://upload.wikimedia.org/wikipedia/commons/b/bc/Unknown_person.jpg",  // URL to the user's profile image
        alt: "Profile picture of Jane Doe"  // Optional alt text for the image
      }
    },
    overallRating: 4,  // Rating given by the user out of 5
    comment: "This residence was absolutely amazing! The location was perfect, the amenities were top-notch, and the host was incredibly friendly. I would definitely recommend this place to anyone visiting the area.",  // Review comment
    reviewDate: "2025-01-07"  // Date when the review was written
  };
  

}
