import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-add-review',
  templateUrl: './add-review.component.html',
  styleUrl: './add-review.component.css'
})
export class AddReviewComponent {
  reviewForm: FormGroup;
  rating: number = 0;
  stars: number[] = [1, 2, 3, 4, 5];

  constructor(private fb: FormBuilder) {
    this.reviewForm = this.fb.group({
      comment: ['']
    });
  }

  rate(star: number) {
    this.rating = star;
  }

  submitReview() {
    // Handle form submission
    console.log('Rating:', this.rating);
    console.log('Comment:', this.reviewForm.value.comment);
  }

  @Output() updateValue = new EventEmitter<void>();
    changeIsShown() {
      this.updateValue.emit(); // No argument is passed
    }

}
