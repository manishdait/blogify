import { BlogState } from "./blog/blog.reducer";
import { CommentState } from "./comment/comment.reducer";

export interface AppState {
  blogs: BlogState,
  comments: CommentState
}