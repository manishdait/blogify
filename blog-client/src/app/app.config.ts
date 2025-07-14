import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideNgxWebstorage, withLocalStorage } from 'ngx-webstorage';
import { routes } from './app.routes';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { AccessTokenInterceptor } from './interceptor/access-token.interceptor';
import { refreshTokenInterceptor } from './interceptor/refresh-token.interceptor';
import { provideState, provideStore } from '@ngrx/store';
import { blogReducer } from './store/blog/blog.reducer';
import { commentReducer } from './store/comment/comment.reducer';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideNgxWebstorage(withLocalStorage()),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    [
      { provide: HTTP_INTERCEPTORS, useClass: refreshTokenInterceptor, multi: true },
      { provide: HTTP_INTERCEPTORS, useClass: AccessTokenInterceptor, multi: true }
    ],
    provideStore(),
    provideState({name: 'blogs', reducer: blogReducer}),
    provideState({name: 'comments', reducer: commentReducer})
  ]
};
