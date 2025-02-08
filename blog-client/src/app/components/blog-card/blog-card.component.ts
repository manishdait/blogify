import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { faComment, IconDefinition } from '@fortawesome/free-solid-svg-icons'
import { BlogResponse } from '../../models/blog';


@Component({
  selector: 'app-blog-card',
  imports: [FontAwesomeModule],
  templateUrl: './blog-card.component.html',
  styleUrl: './blog-card.component.css'
})
export class BlogCardComponent {
  @Input() blog!: BlogResponse;

  facomment: IconDefinition = faComment;

  constructor(private router: Router) {}

  getDetails(blogId: number) {
    this.router.navigate(['/blog/id/' + blogId])
  }
}
