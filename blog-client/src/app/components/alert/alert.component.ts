import { Component } from "@angular/core";
import { AlertService } from "../../service/alert.service";

@Component({
  selector: 'app-alert',
  imports: [],
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.css'
})
export class AlertComponent {
  alert?: string;

  constructor(alertService: AlertService) {
    alertService.alert.subscribe((data) => {
      this.alert = data;
      this.reset();
    });
  }

  reset() {
    window.setTimeout(() => {
      this.alert = undefined;
    }, 5000);
  }
}