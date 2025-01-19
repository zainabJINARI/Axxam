import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReactionService } from '../../../services/reaction.service';

@Component({
  selector: 'app-add-review',
  templateUrl: './add-review.component.html',
  styleUrl: './add-review.component.css'
})
export class AddReviewComponent {
  reviewForm: FormGroup;
  rating: number = 0;
  stars: number[] = [1, 2, 3, 4, 5];

  @Input() announcementId:any;
  @Output() updateValue = new EventEmitter<void>();
  constructor(private fb: FormBuilder,private reactionService:ReactionService) {
    this.reviewForm = this.fb.group({
      comment: ['',Validators.required]
    });
  }

  rate(star: number) {
    this.rating = star;
  }

  submitReview() {
    if (this.rating === 0) {
      alert('Please select a rating before submitting.');
      return;
    }
  
    if (this.reviewForm.invalid) {
      alert('Please enter a comment.');
      return;
    }
  
    // Create FormData
    let formData = new FormData();
    formData.append('ratingValue', this.rating.toString()); // Convert number to string
    formData.append('comment', this.reviewForm.value.comment);
    formData.append('announcementId', this.announcementId);
  
    console.log('Submitting FormData:', formData);
  
    this.reactionService.createReaction(formData).subscribe({
      next: () => {
        alert('Reaction added successfully');
        this.reviewForm.reset('')
        this.rating=0
        this.changeIsShown()
      },
      error: (error) => {
        console.error('Error:', error);
        alert('Error while trying to add reaction');
      }
    });
  }
  

  
    changeIsShown() {
      this.updateValue.emit(); // No argument is passed
    }

}
