import { Component, OnInit } from '@angular/core';
import { BlogResponse } from '../../../../models/blog';
import { BlogService } from '../../../../service/blog.service';
import { ActivatedRoute } from '@angular/router';
import { CommentService } from '../../../../service/comment.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { CommentRequest, CommentResponse } from '../../../../models/comment';
import { CommentCardComponet } from '../../../../components/comment-card/comment-card.component';

@Component({
  selector: 'app-blog-detail',
  imports: [CommentCardComponet, FontAwesomeModule],
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
  comments: CommentResponse[] = [];

  constructor(private blogService: BlogService, private activeRoute: ActivatedRoute, private commentService: CommentService) {
    this.blogId = activeRoute.snapshot.params['blogId'];

    blogService.fetchBlog(this.blogId).subscribe(
      (res) => {
        this.blog = res;
        this.commentService.fetchCommentsForBlog(this.blogId).subscribe(
          (comments)=> {
            this.comments = comments.content;
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
        this.comments.push(res);
      }, (err) => {
        console.log(err);
      }
    )  
  }
}
