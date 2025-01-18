import { Component, OnInit } from '@angular/core';
import { AnnouncementService } from '../../../services/announcement.service';

@Component({
  selector: 'app-explore-section',
  templateUrl: './explore-section.component.html',
  styleUrl: './explore-section.component.css'
})
export class ExploreSectionComponent implements OnInit {
  

  announcements!:any[]
  constructor(private annServ:AnnouncementService){}


  ngOnInit(): void {
    this.annServ.getAnnouncementOrderByRating().subscribe({
      next:(data)=>{
        let result = JSON.parse(JSON.stringify(data))
        this.announcements= result.content
        console.log(this.announcements)

      },
      error:(error)=>{
        console.log('error while trying to get best announcement by rating ')

      }
    })
  }

}
