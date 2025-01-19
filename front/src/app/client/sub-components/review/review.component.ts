import { Component, Input, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { ReactionService } from '../../../services/reaction.service';
import { window } from 'rxjs';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrl: './review.component.css'
  
})
export class ReviewComponent implements OnInit {
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

  @Input() reaction!: any;
  constructor(private authServ:AuthService, private reactionServ:ReactionService){}
  isInteractionAuthrised:boolean=false
  ngOnInit(): void {
     this.isInteractionAuthrised = ((this.reaction.customer.username == this.authServ.getUsername()) || (this.authServ.getRoles()?.includes('ROLE_ADMIN'))) ? true: false
   
  }


  deleteReaction(id:number){
    confirm('Are you sure you want to delete this review ')
    ? 
    this.reactionServ.deleteReaction(id).subscribe({
      next:()=>{
        alert('deleted successfully')
        
      },
      error:(error)=>{
        alert('error while trying to delete ')

      }
    })
    : ''

  }


  

}
