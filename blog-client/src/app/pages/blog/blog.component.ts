import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { LocalStorageService } from 'ngx-webstorage';
import { UserResponse } from '../../models/user';
import { UserService } from '../../service/user.service';
import { fontawsomeIcons } from '../../utils/fa-icons';

@Component({
  selector: 'app-blog',
  imports: [RouterOutlet, RouterLink, FontAwesomeModule],
  templateUrl: './blog.component.html',
  styleUrl: './blog.component.css'
})
export class BlogComponent implements OnInit {
  info: UserResponse = {
    first_name: '',
    last_name: '',
    email: '',
    img_url: null
  };
  authenticated: boolean = false;
  menu: boolean = false;

  constructor (private authService: AuthService, private userService: UserService, private localStorage: LocalStorageService, private router: Router, private faLibrary: FaIconLibrary) {
    if (authService.getAccessToken()) {
      authService.isLoggedIn().subscribe({
        next:(res) => {
          if (res == true) {
            this.authenticated = true;
            userService.userInfo().subscribe({
              next: (res) => {this.info = res}
            });
          } else {
            this.authenticated = false;
            localStorage.clear();
          } 
        }
      });
    } else {
      this.authenticated = false;
      localStorage.clear();
    }
  }

  ngOnInit(): void {
    this.faLibrary.addIcons(...fontawsomeIcons);
  }

  toggleMenu() {
    this.menu = !this.menu;
  }

  profile() {
    this.router.navigate(['profile']);
    this.toggleMenu()
  }

  logout() {
    this.authService.logout();
    window.location.reload();
  }
}
