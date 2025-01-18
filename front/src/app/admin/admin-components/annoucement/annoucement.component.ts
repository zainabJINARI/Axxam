import { Component, OnInit } from '@angular/core';
import { Announcement } from '../../../models/Announcement';
import { AnnouncementService } from '../../../services/announcement.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-annoucement',
  templateUrl: './annoucement.component.html',
  styleUrl: './annoucement.component.css'
})
export class AnnoucementComponent implements OnInit{

  announcements!:Announcement[]
  totalPages:number=0
  currentPage:number=0
  annPerPage:number=5
  

  constructor(private annServ:AnnouncementService, private router:Router){}



  ngOnInit(): void {
    this.loadAnnouncements()
   
  }
  setCurrentPage(page:number){
    this.currentPage=page
    this.loadAnnouncements()

  }

  loadAnnouncements(): void {
    this.annServ.getAnnouncementsByHost(this.currentPage, this.annPerPage).subscribe({
      next:(response: any) => {
        
        let result =JSON.parse(JSON.stringify(response)).totalItems/this.annPerPage 
        
        if(Math.floor(result)!=result){
          result+=1
          result=Math.floor(result)

        }
        this.totalPages=result

        console.log()
        this.announcements=JSON.parse(JSON.stringify(response)).items
        
      },
      error:(error) => {
        console.error(
          'Erreur while trying to get announcemnts ',
          error
        );
      }
  });
  }


  prevPage() {
    this.currentPage-=1
    this.loadAnnouncements()
    
    }
    goToPage(page: number) {
      this.currentPage=page
      this.loadAnnouncements()
    }
    nextPage() {
    this.currentPage+=1
    this.loadAnnouncements()
    }
    
    editAnnouncement(id:string) {
     this.router.navigateByUrl(`admin/edit-announce/${id}`)
    
    }
    deleteAnnouncement(id: string) {
      console.log(id)
       this.annServ.deleteAnnouncement(id).subscribe({
        next:()=>{
          console.log('deleted successfully')
          this.currentPage=0
          this.loadAnnouncements()


        },
        error:(error)=>{
          console.error(error)

        }
       })
    }



}
