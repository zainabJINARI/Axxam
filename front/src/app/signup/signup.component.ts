import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})


export class SignupComponent implements OnInit {
  role: string = 'ROLE_USER';
  public profileForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const roleParam = params['role'];
      if (roleParam === '3') {
        this.role = 'ROLE_HOST';
      } else {
        this.role = 'ROLE_USER';
      }
    });

    this.profileForm = this.fb.group(
      {
        username: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        comfpassword: ['', Validators.required],
      },
      {
        validators: this.passwordsMatchValidator,
      }
    );
  }

  passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('comfpassword')?.value;
    return password === confirmPassword
      ? null
      : { passwordsNotMatching: true };
  }

  saveUser(): void {
    if (this.profileForm.valid) {
      if (
        this.profileForm.value.password !== this.profileForm.value.comfpassword
      ) {
        console.log('Les mots de passe ne correspondent pas');
        return;
      }

      this.authService
        .signup(
          this.profileForm.value.username,
          this.profileForm.value.email,
          this.profileForm.value.password,
          this.role
        )
        .subscribe(
          (response) => {
            this.authService
              .login(
                this.profileForm.value.username,
                this.profileForm.value.password
              )
              .subscribe({
                next: (user) => {
                  console.log(this.authService.getRoles());

                  if (this.authService.getRoles()?.includes('ROLE_USER')) {
                    this.router.navigateByUrl('/client');
                  } else if (
                    this.authService.getRoles()?.includes('ROLE_HOST')
                  ) {
                    this.router.navigateByUrl('/admin/dashboard');
                  } else {
                    this.router.navigateByUrl('/client/details');
                  }
                },
                error: (err) => {
                  // this.errorMessage=err
                  console.log("Erreur lors d'inscription :" + err);
                },
              });
          },
          (error) => {
            console.log("Erreur lors de l'inscription", error);
          }
        );
    } else {
      console.log("Le formulaire n'est pas valide");
    }
  }
}
