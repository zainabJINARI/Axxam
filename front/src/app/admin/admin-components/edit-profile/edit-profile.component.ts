import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css'],
})
export class EditProfileComponent implements OnInit {
  roles: string[] = [];
  fileName: string = 'Upload new photo';

  public profileForm!: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      comfpassword: ['', Validators.required],
    });
    this.loadUserData();
    this.loadRoles();
  }

  loadRoles(): void {
    this.roles = this.authService.getRoles() || [];
  }

  // Fonction pour charger les données de l'utilisateur
  loadUserData(): void {
    const username = this.authService.getUsername();
    console.log('Username :' + username);

    if (username) {
      this.authService.getUserByUserName(username).subscribe(
        (user) => {
          const { username, email } = user;

          this.profileForm.patchValue({
            username: username,
            email: email || '',
            password: '*********',
            comfpassword: '*********',
          });
        },
        (error) => {
          console.error(
            'Erreur lors du chargement des données utilisateur:',
            error
          );
        }
      );
    } else {
      console.warn("Nom d'utilisateur introuvable dans localStorage.");
    }
  }

  updateFileName(event: any): void {
    const inputFile = event.target;
    this.fileName = inputFile.files.length
      ? inputFile.files[0].name
      : 'Aucun fichier sélectionné';
  }

 
}
