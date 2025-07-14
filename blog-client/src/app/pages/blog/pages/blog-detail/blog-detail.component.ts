import { Component, OnInit } from '@angular/core';
import { BlogResponse } from '../../../../models/blog';
import { BlogService } from '../../../../service/blog.service';
import { ActivatedRoute } from '@angular/router';
import { CommentService } from '../../../../service/comment.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { CommentRequest, CommentResponse } from '../../../../models/comment';
import { CommentCardComponet } from '../../../../components/comment-card/comment-card.component';
import { getDate } from '../../../../utils/utils';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../store/app.state';
import { Observable } from 'rxjs';
import { comments } from '../../../../store/comment/comment.selector';
import { addComment, setComments } from '../../../../store/comment/comment.actions';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-blog-detail',
  imports: [CommonModule, CommentCardComponet, FontAwesomeModule],
  templateUrl: './blog-detail.component.html',
  styleUrl: './blog-detail.component.css'
})
export class BlogDetailComponent implements OnInit {
  blogId: number;
  blog: BlogResponse = {
    id: 0,
    title: 'The title top 10 Blogs on Java',
    content: 'rwerwrwe',
    author: 'Author Rambo',
    comments: 2,
    created_at: new Date(),
    edited_at: new Date(),
    author_img: null
  };
  comments: Observable<CommentResponse[]>;

  constructor(private blogService: BlogService, private activeRoute: ActivatedRoute, private store: Store<AppState>, private commentService: CommentService) {
    this.blogId = activeRoute.snapshot.params['blogId'];
    this.comments = store.select(comments);

    blogService.fetchBlog(this.blogId).subscribe(
      (res) => {
        this.blog = res;
        this.commentService.fetchCommentsForBlog(this.blogId).subscribe(
          (cres)=> {
            store.dispatch(setComments({comments: cres.content}));
          }
        );
      }
    );
  }

  ngOnInit(): void {
    
  }

  addComment(comment: string) {
    var request: CommentRequest = {
      blog_id: this.blogId,
      message: comment
    };

    this.commentService.createComment(request).subscribe(
      (res) => {
        this.store.dispatch(addComment({comment: res}))
      }, (err) => {
        console.log(err);
      }
    )  
  }

  getDate(timestamp: any) {
    return getDate(timestamp);
  }
}
