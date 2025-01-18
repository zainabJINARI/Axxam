import { Component, OnInit } from '@angular/core';
import { AnnouncementService } from '../../services/announcement.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-announcement-detail',
  templateUrl: './announcement-detail.component.html',
  styleUrl: './announcement-detail.component.css'
})
export class AnnouncementDetailComponent implements OnInit {
   
   public isShown !: boolean 
   announcement:any
   constructor(private annSer:AnnouncementService, private router:ActivatedRoute){}
   

   flipIsShown(){
    this.isShown=!this.isShown
   }


   ngOnInit(): void {
    this.annSer.getAnnouncementById(this.router.snapshot.paramMap.get('id') || '').subscribe({
      next:(data)=>{
        this.announcement=data

      },
      
    })
  }


}
