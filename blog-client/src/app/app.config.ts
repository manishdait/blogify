import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideNgxWebstorage, withLocalStorage } from 'ngx-webstorage';
import { routes } from './app.routes';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { AccessTokenInterceptor } from './interceptor/access-token.interceptor';
import { refreshTokenInterceptor } from './interceptor/refresh-token.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideNgxWebstorage(withLocalStorage()),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    [
      {provide: HTTP_INTERCEPTORS, useClass: refreshTokenInterceptor, multi: true},
      {provide: HTTP_INTERCEPTORS, useClass: AccessTokenInterceptor, multi: true}
    ]
  ]
};
