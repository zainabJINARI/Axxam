import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-card-announcement',
  templateUrl: './card-announcement.component.html',
  styleUrl: './card-announcement.component.css'
})
export class CardAnnouncementComponent {

  @Input() announcement!: any;
  constructor(private router:Router){}


  goToDetails(){
    this.router.navigateByUrl(`client/announcement/${this.announcement.id}`)

  }

}
