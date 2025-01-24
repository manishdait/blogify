import { Component, EventEmitter, Output } from "@angular/core";
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../../service/auth.service";
import { AuthRequest } from "../../models/auth";
import { AlertService } from "../../../service/alert.service";

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponet {
  form: FormGroup;
  formError: boolean = false;

  @Output('createAccount') createAccount: EventEmitter<boolean> = new EventEmitter<boolean>();
    
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

    this.authService.authenticateUser(request).subscribe(
      (res) => {
        this.router.navigate(['blog'])
      }, (err) => {
        console.log(err);
        this.alertService.alert = 'Error Login'
      }
    );
  }
  
  toggleCreateAccount() {
    this.createAccount.emit(true);
  }
}