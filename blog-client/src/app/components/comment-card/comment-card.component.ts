import { Component, Input, OnInit } from "@angular/core";
import { CommentRequest, CommentResponse } from "../../models/comment";
import { CommentService } from "../../service/comment.service";
import { getDate } from "../../utils/utils";
import { FaIconLibrary, FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { fontawsomeIcons } from "../../utils/fa-icons";
import { Store } from "@ngrx/store";
import { AppState } from "../../store/app.state";
import { removeComment, updateComment } from "../../store/comment/comment.actions";

@Component({
  selector: '<app-comment>',
  imports: [FontAwesomeModule],
  templateUrl: './comment-card.component.html',
  styleUrl: './comment-card.component.css'
})
export class CommentCardComponet implements OnInit {
  @Input('comment') comment!: CommentResponse;

  message: string = "";

  updateToggle: boolean = false;

  constructor(private commentService: CommentService, private store: Store<AppState>, private faLibrary: FaIconLibrary) {
    
  }

  ngOnInit(): void {
    this.faLibrary.addIcons(...fontawsomeIcons)
  }

  updateComment(comment: string) {
    const request: CommentRequest = {
      blog_id: 0,
      message: comment
    }

    this.commentService.updateComment(this.comment.id, request).subscribe({
      next: (res) => {
        this.store.dispatch(updateComment({comment: res}))
        this.comment = res;
        this.toggleUpdate();
      }
    });
  }

  toggleUpdate() {
    this.updateToggle = !this.updateToggle;
  }

  deleteComment() {
    this.commentService.deleteComment(this.comment.id).subscribe({
      next: () => {
        this.store.dispatch(removeComment({id: this.comment.id}))
      }
    });
  }

  getDate(timestamp: any) {
    return getDate(timestamp);
  }
}