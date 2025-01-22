import { Component, OnInit } from '@angular/core';
import { AnnouncementService } from '../../services/announcement.service';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-announcement-detail',
  templateUrl: './announcement-detail.component.html',
  styleUrl: './announcement-detail.component.css',
})
export class AnnouncementDetailComponent implements OnInit {
  public isShown!: boolean;
  announcement: any;
  isReacted!: boolean;
  public isLoading: boolean = false;

  constructor(
    private annSer: AnnouncementService,
    private router: ActivatedRoute,
    private autuServ: AuthService
  ) {}

  flipIsShown() {
    this.isShown = !this.isShown;
  }

  ngOnInit(): void {
    this.isLoading=true
    this.annSer
      .getAnnouncementById(this.router.snapshot.paramMap.get('id') || '')
      .subscribe({
        next: (data) => {
          this.announcement = data;
          console.log(this.autuServ.getUsername());
          // this.announcement.reactions.find((r:any)=>{r.username== this.autuServ.getUsername()})
          this.isReacted = this.announcement.reactions.find((r: any) => {
            return r.customer.username == this.autuServ.getUsername();
          })
            ? true
            : false;
          this.isReacted = this.autuServ.getRoles()?.includes('ROLE_USER')
            ? this.isReacted
            : true;

            this.isLoading=false

           
        },
      });
  }
}
