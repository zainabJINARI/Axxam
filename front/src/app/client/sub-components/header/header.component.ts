import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent implements OnInit {
  isLogged: boolean = false; // Statut de connexion de l'utilisateur
  showLogoutOptions: boolean = false; // Affichage des options de déconnexion

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // Vérifiez si l'utilisateur est connecté au démarrage
    const token = this.authService.getToken();
    if (token) {
      this.isLogged = true;
    }
  }

  toggleLogoutOptions(): void {
    // Inverser l'état d'affichage des options de déconnexion
    this.showLogoutOptions = !this.showLogoutOptions;
  }

  @HostListener('document:click', ['$event'])
  closeLogoutOptions(event: MouseEvent): void {
    const clickedInside = (event.target as HTMLElement).closest(
      '.profile-container'
    );
    if (!clickedInside) {
      this.showLogoutOptions = false;
    }
  }

  onLogout(): void {
    this.authService.logout();
  }
}
