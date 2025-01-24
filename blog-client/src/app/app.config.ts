import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideNgxWebstorage, withLocalStorage } from 'ngx-webstorage';
import { routes } from './app.routes';
import { HTTP_INTERCEPTORS, provideHttpClient, withFetch, withInterceptors, withInterceptorsFromDi } from '@angular/common/http';
import { AuthInterceptor } from './interceptor/auth.interceptor';
import { ErrorInterceptor } from './interceptor/error.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideNgxWebstorage(withLocalStorage()),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    [
      {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
      {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
    ]
  ]
};
