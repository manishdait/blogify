import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { AlertService } from '../../service/alert.service';
import { AuthService } from '../../service/auth.service';
import { fontawsomeIcons } from '../../utils/fa-icons';

@Component({
  selector: 'app-verification',
  imports: [ReactiveFormsModule, FontAwesomeModule],
  templateUrl: './verification.component.html',
  styleUrl: './verification.component.css'
})
export class VerificationComponent implements OnInit {
  formError: boolean = false;
  verified: boolean = false;

  username: string;
  form: FormGroup;
  
  constructor(private authService: AuthService, private alertService: AlertService, private activeRoute: ActivatedRoute, private router: Router, private faLibrary: FaIconLibrary) {
    this.username = activeRoute.snapshot.params['username'];
    this.form = new FormGroup({
      code: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(6)])
    });
  }

  ngOnInit(): void {
    this.faLibrary.addIcons(...fontawsomeIcons);
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

    this.authService.verifyAccount(this.username, code).subscribe({
      next: () => {this.verified = true;},
      error: (err) => {this.alertService.alert = err.error.error;}
    });
  }

  resend() {
    this.authService.resendVerificationToken(this.username).subscribe({
      next: () => {this.alertService.alert = "Email send to your inbox";},
      error: (err) => {this.alertService.alert = err.error.error;}
    });
  }

  continue() {
    this.router.navigate(['blog'], {replaceUrl: true});
  }
}
