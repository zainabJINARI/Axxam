import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ReactionService } from '../../../services/reaction.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent  implements OnInit{
  notifications:any
  currentPage:number=0
  totalNbrOfPages!:number
  notifPerPage:number=10
  constructor(private reactionService: ReactionService,private router: Router){}


    @Output() updateValue = new EventEmitter<void>();
    
    changeIsShown() {
      console.log('click')
      this.updateValue.emit(); 
    }

    goToReview(id:string,idReaction:number){
      this.reactionService.updateNotificationStatus(idReaction).subscribe({
        next:(result)=>{
          if(result){
            this.router.navigateByUrl(`/client/announcement/${id}`)

          }else{
            alert('something went wrong')
          }

        },
        error:(error)=>{
          console.log('error while trying to flip notitfication to rzad ',error)

        }
      })
     

    }
  

  ngOnInit(): void {
    this.loadNotifications()
  }

  prevPage() {
    this.currentPage-=1
    this.loadNotifications()
    
    }
    goToPage(page: number) {
      this.currentPage=page
      this.loadNotifications()
    }
    nextPage() {
    this.currentPage+=1
    this.loadNotifications()
    }

    loadNotifications(){
      this.reactionService.getAllUnreadReactionsByHost(this.currentPage,this.notifPerPage).subscribe({
        next:(data)=>{
          this.notifications= JSON.parse(JSON.stringify(data)).items

          let result =JSON.parse(JSON.stringify(data)).totalItems/this.notifPerPage 
        
          if(Math.floor(result)!=result){
            result+=1
            result=Math.floor(result)
  
          }
          this.totalNbrOfPages=result

  
  
        }
      })

    }

    


}
