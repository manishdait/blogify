import { Component, Input } from "@angular/core";
import { CommentRequest, CommentResponse } from "../../models/comment";
import { CommentService } from "../../service/comment.service";

@Component({
  selector: '<app-comment>',
  imports: [],
  templateUrl: './comment-card.component.html',
  styleUrl: './comment-card.component.css'
})
export class CommentCardComponet {
  @Input('comment') comment!: CommentResponse;
  message: string = "";

  constructor(private commentService: CommentService) {}

  updateComment(comment: string) {
    
    const request: CommentRequest = {
      blog_id: 0,
      message: comment
    }

    this.commentService.updateComment(this.comment.id, request).subscribe({
      next: (res) => {this.comment = res;}
    });
  }

  deleteComment() {
    this.commentService.deleteComment(this.comment.id).subscribe({
      next: () => {
        window.location.reload();
      }
    });
  }
}