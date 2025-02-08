import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RegistrationRequest } from '../../models/registration';
import { AlertService } from '../../service/alert.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-registration',
  imports: [ReactiveFormsModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {
  form: FormGroup;
  formError: boolean = false;

  constructor (private authService: AuthService, private alertService: AlertService, private router: Router) {
    this.form = new FormGroup({
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
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
    
    var request: RegistrationRequest = {
      first_name: this.form.get('firstName')?.value,
      last_name: this.form.get('lastName')?.value,
      email: this.form.get('email')?.value,
      password: this.form.get('password')?.value
    }

    this.form.reset;

    this.authService.register(request).subscribe({
      next: () => {
        this.router.navigate(['blog'], {replaceUrl: true});
      },
      error: (err) => {
        this.alertService.alert = err.error.error;
      }
    });
  }

  login() {
    this.router.navigate(['login'], {replaceUrl: true})
  }
}
