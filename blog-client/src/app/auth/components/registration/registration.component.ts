import { Component, EventEmitter, Output } from "@angular/core";
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../../service/auth.service";
import { RegistrationRequest } from "../../models/registration";
import { AlertService } from "../../../service/alert.service";

@Component({
  selector: 'app-registration',
  imports: [ReactiveFormsModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {
  form: FormGroup;
  formError: boolean = false;

  @Output('createAccount') createAccount: EventEmitter<boolean> = new EventEmitter<boolean>();
  
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
      firstName: this.form.get('firstName')?.value,
      lastName: this.form.get('lastName')?.value,
      email: this.form.get('email')?.value,
      password: this.form.get('password')?.value
    }

    this.form.reset;

    this.authService.registerUser(request).subscribe(
      (res) => {
        this.router.navigate(['blog']);
      }, (err) => {
        console.log(err);
        this.alertService.alert = err.error.error;
      }
    );
  }

  toggleCreateAccount() {
    this.createAccount.emit(false);
  }
}