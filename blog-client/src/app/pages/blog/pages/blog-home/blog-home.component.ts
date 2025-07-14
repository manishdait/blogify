import { Component } from '@angular/core';
import { BlogService } from '../../../../service/blog.service';
import { BlogResponse } from '../../../../models/blog';
import { BlogCardComponent } from '../../../../components/blog-card/blog-card.component';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../store/app.state';
import { Observable } from 'rxjs';
import { blogs } from '../../../../store/blog/blog.selector';
import { CommonModule } from '@angular/common';
import { setBlogs } from '../../../../store/blog/blog.actions';

@Component({
  selector: 'app-blog-home',
  imports: [CommonModule, BlogCardComponent],
  templateUrl: './blog-home.component.html',
  styleUrl: './blog-home.component.css'
})
export class BlogHomeComponent {
  blogs$: Observable<BlogResponse[]>

  constructor(private blogService: BlogService, private store: Store<AppState>) {
    this.blogs$ = store.select(blogs);
    blogService.fetchBlogs().subscribe((res) => {
      store.dispatch(setBlogs({blogs: res.content}))
    });
  }
}
