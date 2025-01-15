import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  loginFormGroup!: FormGroup
  errorMessage!:string



  constructor( private fb: FormBuilder
    ,private auth:AuthService,
  private router: Router){}


  ngOnInit(): void {
    this.loginFormGroup= this.fb.group({
      username:this.fb.control(""),
      password:this.fb.control('')

    })
  }

  handleLogin() {
    let username = this.loginFormGroup.value.username
    let password = this.loginFormGroup.value.password
    this.auth.login(username,password).subscribe({
      next:(user)=>{
        console.log(this.auth.getRoles())
        
        if(this.auth.getRoles()?.includes('ROLE_USER')){
          this.router.navigateByUrl('/client')

        }else if(this.auth.getRoles()?.includes('ROLE_HOST')){

          this.router.navigateByUrl('/admin/dashboard')
        }else{
          this.router.navigateByUrl('/client/details')
        }

      },
      error:(err)=>{
        this.errorMessage=err
      }
    })
    
  }
  

}