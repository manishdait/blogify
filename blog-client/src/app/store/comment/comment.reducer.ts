import { createReducer, on } from "@ngrx/store";
import { CommentResponse } from "../../models/comment";
import { addComment, removeComment, setComments, updateComment } from "./comment.actions";

export interface CommentState {
  comments: CommentResponse[]
}

export const initialState: CommentState = {
  comments: []
}

export const commentReducer = createReducer(
  initialState,
  on(setComments, (state, {comments}) => ({
    ...state,
    comments: comments
  })),
  on(addComment, (state, {comment}) => ({
    ...state,
    comments: [...state.comments, comment]
  })),
  on(updateComment, (state, {comment}) => ({
    ...state,
    comments: state.comments.map(c => c.id !== comment.id? c : comment)
  })),
  on(removeComment, (state, {id}) => ({
    ...state,
    comments: state.comments.filter(c => c.id !== id)
  }))
)