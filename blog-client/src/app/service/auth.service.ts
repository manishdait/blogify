import { HttpBackend, HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { LocalStorageService } from 'ngx-webstorage';
import { AuthResponse, AuthRequest } from '../models/auth';
import { RegistrationRequest } from '../models/registration';
import { environment } from '../../environments/environment';
import { jwtDecode } from 'jwt-decode';

const URL = `${environment.API_DOMAIN}/auth`;

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  client: HttpClient

  constructor(private backend: HttpBackend, private localStorage: LocalStorageService, private interceptorClient: HttpClient) {
    this.client = new HttpClient(backend);
  }

  register(request: RegistrationRequest): Observable<AuthResponse> {
    return this.client.post<AuthResponse>(`${URL}/sign-up`, request).pipe(
      map(response => {
        this.storeCred(response);
        return response; 
      })
    );
  }

  authenticate(request: AuthRequest): Observable<AuthResponse> {
    return this.client.post<AuthResponse>(`${URL}/login`, request).pipe(
      map(response => {
        this.storeCred(response);
        return response; 
      })
    );
  }

  verifyAccount(username: string, activationCode: string): Observable<Map<string, boolean>> {
    return this.client.post<Map<string, boolean>>(`${URL}/verify/${username}/${activationCode}`, null);
  }

  resendVerificationToken(username: string): Observable<Map<string, boolean>> {
    return this.client.post<Map<string, boolean>>(`${URL}/resend-token/${username}`, null);
  }

  refreshToken(): Observable<AuthResponse> {
    return this.client.post<AuthResponse>(`${URL}/refresh`, null, {headers: new HttpHeaders({'Authorization': `Bearer ${this.localStorage.retrieve('refreshToken')}`})}).pipe(
      map(response => {
        this.storeCred(response);
        return response;
      })
    );
  }

  logout() {
    this.localStorage.clear();
  }

  storeCred(res: AuthResponse) {
    this.localStorage.store('username', res.username);
    this.localStorage.store('accessToken', res.access_token);
    this.localStorage.store('refreshToken', res.refresh_token);
  }

  getAccessToken() {
    return this.localStorage.retrieve('accessToken');
  }

  isLoggedIn():Observable<boolean> {
    if (this.localStorage.retrieve('accessToken') == null) {
      return of(false)
    }
    
    return this.interceptorClient.get<Map<string, boolean>>(`http://localhost:8080/blog-api/v1/validate-access`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }
}
