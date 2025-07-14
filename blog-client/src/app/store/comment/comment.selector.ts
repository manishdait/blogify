import { createSelector } from "@ngrx/store";
import { AppState } from "../app.state";

export const selectCommentState = (state: AppState) => state.comments;

export const comments = createSelector(
  selectCommentState,
  (state) => state.comments
)