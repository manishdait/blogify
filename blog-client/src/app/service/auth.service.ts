import { HttpBackend, HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { LocalStorageService } from 'ngx-webstorage';
import { AuthResponse, AuthRequest } from '../auth/models/auth';
import { RegistrationRequest } from '../auth/models/registration';
import { environment } from '../../environments/environment.development';

const URL = `${environment.API_DOMAIN}/auth`;

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  client: HttpClient;

  constructor(private backend: HttpBackend, private localStorage: LocalStorageService, private secureClient: HttpClient) {
    this.client = new HttpClient(backend);
  }

  registerUser(request: RegistrationRequest): Observable<AuthResponse> {
    return this.client.post<AuthResponse>(`${URL}/sign-up`, request).pipe(
      map(response => {
        this.storeCred(response);
        return response; 
      })
    );
  }

  authenticateUser(request: AuthRequest): Observable<AuthResponse> {
    return this.client.post<AuthResponse>(`${URL}/login`, request).pipe(
      map(response => {
        this.storeCred(response);
        return response; 
      })
    );;
  }

  verifyUser(username: string, activationCode: string): Observable<Map<string, boolean>> {
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

  storeCred(res: AuthResponse) {
    this.localStorage.store('username', res.username);
    this.localStorage.store('accessToken', res.accessToken);
    this.localStorage.store('refreshToken', res.refreshToken);
  }

  getAccessToken() {
    return this.localStorage.retrieve('accessToken');
  }

  isLoggedIn():Observable<boolean> {
    if (this.localStorage.retrieve('accessToken') == null) {
      return of(false)
    }

    return this.secureClient.get<Map<string, boolean>>(`http://localhost:8080/blog-api/v1/validate-access`).pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }
}
