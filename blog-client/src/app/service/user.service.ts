import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { UserRequest, UserResponse } from '../models/user';

const URL = `${environment.API_DOMAIN}/user`;

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private client: HttpClient) { }

  userInfo(): Observable<UserResponse> {
    return this.client.get<UserResponse>(`${URL}/info`);
  }

  upadteDetails(request: UserRequest): Observable<UserResponse> {
    return this.client.put<UserResponse>(`${URL}/update`, request);
  }

  upadteAvtar(file: File): Observable<UserResponse> {
    const formdata: FormData = new FormData;
    formdata.append('image', file)
    return this.client.put<UserResponse>(`${URL}/update-avtar`, formdata);
  }
}
