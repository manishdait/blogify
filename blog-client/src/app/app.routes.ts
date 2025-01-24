import { Routes } from '@angular/router';
import { BlogHomeComponent } from './blog/pages/blog-home/blog-home.component';
import { BlogComponent } from './blog/blog.component';
import { BlogDetailComponent } from './blog/pages/blog-detail/blog-detail.component';
import { VerificationComponent } from './verification/verification.component';
import { BlogFormComponent } from './blog/pages/blog-form/blog-form.component';
import { AuthComponent } from './auth/auth.component';
import { authGuard } from './auth.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'blog'
  },
  {
    path: 'register',
    component: AuthComponent
  },
  {
    path: ':username/verify',
    component: VerificationComponent
  },
  {
    path: 'blog',
    component: BlogComponent, 
    children: [
      {path: '', component: BlogHomeComponent},
      {path: 'id/:blogId', component: BlogDetailComponent, canActivate:[authGuard]},
      {path: 'create', component: BlogFormComponent, canActivate:[authGuard]}
    ],
  }
];
