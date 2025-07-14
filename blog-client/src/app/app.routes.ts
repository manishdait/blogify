import { Routes } from '@angular/router';
import { BlogHomeComponent } from './pages/blog/pages/blog-home/blog-home.component';
import { BlogComponent } from './pages/blog/blog.component';
import { BlogDetailComponent } from './pages/blog/pages/blog-detail/blog-detail.component';
import { BlogFormComponent } from './pages/blog/pages/blog-form/blog-form.component';
import { authGuard } from './auth.guard';
import { VerificationComponent } from './pages/email-verification/verification.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { RegistrationComponent } from './pages/registration/registration.component';
import { LoginComponent } from './pages/login/login.component';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'blog'
  },
  {
    path: 'register',
    component: RegistrationComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'verify',
    component: VerificationComponent
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [authGuard]
  },
  {
    path: 'blog',
    component: BlogComponent, 
    children: [
      {path: '', component: BlogHomeComponent},
      {path: 'id/:blogId', component: BlogDetailComponent, canActivate: [authGuard]},
      {path: 'create', component: BlogFormComponent, canActivate: [authGuard]}
    ],
  }
];
