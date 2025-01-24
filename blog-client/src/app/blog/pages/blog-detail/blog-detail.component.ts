import { Component } from '@angular/core';
import { BlogResponse } from '../../models/blog';
import { BlogService } from '../../../service/blog.service';
import { ActivatedRoute } from '@angular/router';
import { CommentService } from '../../../service/comment.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { IconDefinition } from '@fortawesome/free-solid-svg-icons'
import { Icons } from '../../../utils/models/icons';
import { CommentCardComponet } from '../../components/comment-card/comment-card.component';
import { CommentRequest, CommentResponse } from '../../models/comment';

@Component({
  selector: 'app-blog-detail',
  imports: [CommentCardComponet, FontAwesomeModule],
  templateUrl: './blog-detail.component.html',
  styleUrl: './blog-detail.component.css'
})
export class BlogDetailComponent {
  blogId: number;
  blog: BlogResponse = {
    id: 0,
    title: '',
    content: '',
    author: '',
    comments: 0,
    createdAt: new Date()
  };
  
  commentIcon: IconDefinition = Icons['comment'];
  comments: CommentResponse[] = [];

  constructor(private blogService: BlogService, private activeRoute: ActivatedRoute, private commentService: CommentService) {
    this.blogId = activeRoute.snapshot.params['blogId'];

    blogService.fetchBlog(this.blogId).subscribe(
      (res) => {
        this.blog = res;
        commentService.fetchCommentsForBlog(this.blogId).subscribe(
          (comments)=> {
            this.comments = comments;
          }
        );
      }
    );
  }

  addComment(comment: string) {
    var request: CommentRequest = {
      blogId: this.blogId,
      message: comment
    };

    this.commentService.createComment(request).subscribe(
      (res) => {
        this.comments.push(res);
      }, (err) => {
        console.log(err);
      }
    )  
  }
}
