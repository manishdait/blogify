import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service';
import { UserRequest, UserResponse } from '../../models/user';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fontawsomeIcons } from '../../utils/fa-icons';

@Component({
  selector: 'app-profile',
  imports: [FontAwesomeModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  info: UserResponse = {
    first_name: '',
    last_name: '',
    email: '',
    img_url: null
  };
  changes: boolean = false;
  fileReader: FileReader;

  newImg: string = "";
  updateImage: boolean = false;
  updateInfo: boolean = false;

  file: File | null = null;

  constructor (private userService: UserService, private faLibrary: FaIconLibrary) {
    userService.userInfo().subscribe(
      (res) => {this.info = res}
    );

    this.fileReader = new FileReader();
  }

  ngOnInit(): void {
    this.faLibrary.addIcons(...fontawsomeIcons);
  }

  upload(event: Event) {
    const target = event.target as HTMLInputElement;
    const files = target.files;

    this.file = files![0];

    this.fileReader.onload = () => {
      this.newImg = this.fileReader.result?.toString()!;
    }

    this.fileReader.readAsDataURL(files![0]);
  }

  unload() {
    this.newImg = '';
  }

  toggleUpdateImage() {
    this.updateImage = !this.updateImage;
  }

  toggleUpdateInfo() {
    this.updateInfo = !this.updateInfo;
  }

  updateDetails(firstname: string, lastname: string) {
    const request: UserRequest = {
      first_name: firstname,
      last_name: lastname
    }

    this.userService.upadteDetails(request).subscribe({
      next: (res) => {this.info = res;}
    })
  }

  updateAvtar() {
    if (this.file) {
      this.userService.upadteAvtar(this.file).subscribe({
        next: (res) => {this.info = res;}
      })
    }
  }
}
