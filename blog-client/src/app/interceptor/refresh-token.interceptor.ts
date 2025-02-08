import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, switchMap, throwError } from 'rxjs';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';

@Injectable()
export class refreshTokenInterceptor implements HttpInterceptor {
  errorCount: number  = 0;

  constructor(private authService: AuthService, private router: Router) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError(err => {
        if (err instanceof HttpErrorResponse && err.status == 401) {
          if (!request.url.includes('/login') && this.errorCount < 2) {
            this.errorCount++;
            return this.handle401err(request, next);
          } else {
            this.router.navigate(['register'])
            throw throwError(() => err)
          }
        } else {
          throw throwError(() => err)
        }
      })
    );
  }

  addToken(request: HttpRequest<unknown>): HttpRequest<any> {
    const accessToken = this.authService.getAccessToken();

    if (accessToken) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${accessToken}`
        }
      });
    }

    return request;
  }


  handle401err(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return this.authService.refreshToken().pipe(
      switchMap(() => {
        return next.handle(this.addToken(request));
      }),
    );
  }
}

