import { Component, Input } from '@angular/core';
import { BlogResponse } from '../../models/blog';
import { Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { IconDefinition } from '@fortawesome/free-solid-svg-icons'
import { Icons } from '../../../utils/models/icons';


@Component({
  selector: 'app-blog-card',
  imports: [FontAwesomeModule],
  templateUrl: './blog-card.component.html',
  styleUrl: './blog-card.component.css'
})
export class BlogCardComponent {
  @Input() blog!: BlogResponse;

  commentIcon: IconDefinition = Icons['comment'];

  constructor(private router: Router) {}

  getDetails(blogId: number) {
    this.router.navigate(['/blog/id/' + blogId])
  }
}
