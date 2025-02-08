import { Component } from '@angular/core';
import { BlogService } from '../../../../service/blog.service';
import { BlogResponse } from '../../../../models/blog';
import { BlogCardComponent } from '../../../../components/blog-card/blog-card.component';

@Component({
  selector: 'app-blog-home',
  imports: [BlogCardComponent],
  templateUrl: './blog-home.component.html',
  styleUrl: './blog-home.component.css'
})
export class BlogHomeComponent {
  blogs: BlogResponse[] = [];

  constructor(private blogService: BlogService) {
    blogService.fetchBlogs().subscribe((res) => {
      this.blogs = res.content;
    });
  }
}
