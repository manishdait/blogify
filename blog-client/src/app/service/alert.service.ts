import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  private $alert: Subject<string> = new Subject<string>();

  set alert(message: string) {
    this.$alert.next(message);
  }

  get alert(): Observable<string>  {
    return this.$alert.asObservable()
  }
}