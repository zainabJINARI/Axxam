import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-genenral-info-ann',
  templateUrl: './genenral-info-ann.component.html',
  styleUrl: './genenral-info-ann.component.css'
})
export class GenenralInfoAnnComponent {
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

  @Input() announcement!: any;
  
  @Output() updateValue = new EventEmitter<void>();
  changeIsShown() {
    this.updateValue.emit(); // No argument is passed
  }

}
