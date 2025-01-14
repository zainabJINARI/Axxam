import { Component } from '@angular/core';

@Component({
  selector: 'app-announcement-detail',
  templateUrl: './announcement-detail.component.html',
  styleUrl: './announcement-detail.component.css'
})
export class AnnouncementDetailComponent {
   public isShown !: boolean 
   flipIsShown(){
    this.isShown=!this.isShown
   }

}
