import { Component, Input } from "@angular/core";
import { CommentResponse } from "../../models/comment";

@Component({
  selector: '<app-comment>',
  imports: [],
  templateUrl: './comment-card.component.html',
  styleUrl: './comment-card.component.css'
})
export class CommentCardComponet {
  @Input('comment') comment!: CommentResponse;
}