import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { IconDefinition } from '@fortawesome/free-solid-svg-icons'
import { AuthService } from '../service/auth.service';
import { AlertService } from '../service/alert.service';
import { Icons } from '../utils/models/icons';

@Component({
  selector: 'app-verification',
  imports: [ReactiveFormsModule, FontAwesomeModule],
  templateUrl: './verification.component.html',
  styleUrl: './verification.component.css'
})
export class VerificationComponent {
  formError: boolean = false;
  verified: boolean = false;
  username: string;
  checkIcon: IconDefinition = Icons['check'];

  form: FormGroup;
  
  constructor(private authService: AuthService, private alertService: AlertService, private activeRoute: ActivatedRoute, private router: Router) {
    this.username = activeRoute.snapshot.params['username'];

    this.form = new FormGroup({
      code: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(6)])
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
    var code: string = this.form.get('code')!.value;
    this.form.reset;

    this.authService.verifyUser(this.username, code).subscribe(
      (res) => {
        this.verified = true;
      }, (err) => {
        console.log(err);
        this.alertService.alert = 'Error Verifying';
      }
    );
  }

  resend() {
    this.authService.resendVerificationToken(this.username).subscribe(
      (res) => {
        this.alertService.alert = 'Email send to your email address'
      }, (err) => {
        console.log(err);
        this.alertService.alert = 'Error Resend';
      }
    );
  }

  continue() {
    this.router.navigate(['blog']);
  }
}
