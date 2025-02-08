import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthRequest } from '../../models/auth';
import { AlertService } from '../../service/alert.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  form: FormGroup;
  formError: boolean = false;

  constructor (private authService: AuthService, private alertService: AlertService, private router: Router) {
    this.form = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)])
    });
  }
  
  get formControls() {
    return this.form.controls;
  }
  
  onSubmit() {
    if (this.form.invalid) {
      this.formError = true;
      return;
    }
  
    this.formError = false;
    var request: AuthRequest = {
      email: this.form.get('email')?.value,
      password: this.form.get('password')?.value
    };
    this.form.reset;

    this.authService.authenticate(request).subscribe({
      next: () => {this.router.navigate(['blog']);},
      error: (err) => {this.alertService.alert = err.error.error;}
    });
  }
  
  createAccount() {
   this.router.navigate(['register'], {replaceUrl: true});
  }
}
