import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { BlogResponse } from '../../models/blog';
import { getDate } from '../../utils/utils';
import { fontawsomeIcons } from '../../utils/fa-icons';


@Component({
  selector: 'app-blog-card',
  imports: [FontAwesomeModule],
  templateUrl: './blog-card.component.html',
  styleUrl: './blog-card.component.css'
})
export class BlogCardComponent implements OnInit {
  @Input() blog!: BlogResponse;

  constructor(private router: Router, private faLibrary: FaIconLibrary) {}
  ngOnInit(): void {
    this.faLibrary.addIcons(...fontawsomeIcons);
  }

  getDetails(blogId: number) {
    this.router.navigate(['/blog/id/' + blogId])
  }

  getDate(timestamp: any) {
    return getDate(timestamp);
  }
}
