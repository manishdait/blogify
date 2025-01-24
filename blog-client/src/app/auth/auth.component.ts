import { Component } from "@angular/core";
import { RegistrationComponent } from "./components/registration/registration.component";
import { LoginComponet } from "./components/login/login.component";

@Component({
  selector: 'app-auth',
  imports: [RegistrationComponent, LoginComponet],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent {
  createAccount: boolean = false;

  toggleCreateAccount(event: boolean) {
    this.createAccount = event;
  }
}