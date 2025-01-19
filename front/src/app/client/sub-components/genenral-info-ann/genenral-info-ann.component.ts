import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-genenral-info-ann',
  templateUrl: './genenral-info-ann.component.html',
  styleUrl: './genenral-info-ann.component.css'
})
export class GenenralInfoAnnComponent  implements OnInit{
  public  residenceInfo = {
    location: "New York",  // Example location
    description: "This beautiful residence is located in the heart of the city, offering a perfect blend of modernity and comfort. It comes with all the necessary amenities to ensure a pleasant stay, including high-speed Wi-Fi, air conditioning, and a fully-equipped kitchen. Whether you're here for business or leisure, this residence offers a peaceful escape with stunning city views, making it the ideal place to call home during your visit.",
    generalRating: 4.5,  // Rating out of 5
    nbrOfComments: 150,  // Number of comments
    services: ["Wi-Fi", "Air Conditioning", "Parking", "Laundry", "Swimming Pool","Swimming Pool"],  // List of services offered
    hostInfo: {
      name: "John Doe",  // Host's name
      creationDate: "2020-05-15",  // Account creation date
      img: {
        src: "https://upload.wikimedia.org/wikipedia/commons/b/bc/Unknown_person.jpg",  // Image source URL
        alt: "Host profile image"  // Optional alt text for the image
      }
    }
  };

  constructor(){}

  @Input() announcement!: any;
  


  totalRating: number = 0;
  overallRating: string = '0';
  @Input()  isReacted:boolean = false

  
  ngOnInit() {
    if (this.announcement?.reactions && this.announcement.reactions.length > 0) {
      this.totalRating = this.announcement.reactions.reduce((sum: number, reaction: any) => sum + reaction.ratingValue, 0);
      this.overallRating =(this.totalRating / this.announcement.reactions.length).toFixed(1);

     
    } else {
      this.totalRating = 0;
      this.overallRating = '0'; // or set to a default value like 0
    }
  }
  
  @Output() updateValue = new EventEmitter<void>();
  changeIsShown() {
    this.updateValue.emit(); // No argument is passed
  }

}
