import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class HostGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.authService.isAuthenticated() && this.authService.isHost()) {
      return true;
    }else if(this.authService.isAuthenticated() ){

    }
    this.router.navigate(['/client']); // Redirect to client section if not a host
    return false;
  }
}
