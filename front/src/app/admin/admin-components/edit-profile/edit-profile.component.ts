import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css'],
})
export class EditProfileComponent implements OnInit {
  roles: string[] = [];
  fileName: string = 'Upload new photo';

  public profileForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router , 
    private toastrService: ToastrService
  ) {}

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
            password: '',
            comfpassword: '',
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

  // Fonction pour mettre à jour les données utilisateur
  editProfile(): void {
    const { username, email, password, comfpassword } = this.profileForm.value;

    // Vérifier que les mots de passe correspondent
    if (password !== comfpassword) {
      console.error('Les mots de passe ne correspondent pas.');
      return;
    }

    // Appeler la méthode updateUser avec les nouvelles informations
    this.authService.updateUser(username, email, password).subscribe(
      (response) => {
        this.router.navigate(['/admin/dashboard']);
        this.toastrService.success('Profil mis à jour avec succès');
      },
      (error) => {
        console.error('Erreur lors de la mise à jour du profil', error);
        this.toastrService.error('Erreur lors de la mise à jour du profil');
      }
    );
  }

  cancelEdit(): void {
    if (this.profileForm.dirty) {
      const confirmed = confirm(
        'You have unsaved changes. Are you sure you want to cancel?'
      );
      if (confirmed) {
        this.profileForm.reset(this.profileForm);
        this.router.navigateByUrl('admin/profile');
      }
    } else {
      this.router.navigateByUrl('admin/dashboard');
    }
  }
}
