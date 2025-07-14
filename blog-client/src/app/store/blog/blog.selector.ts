import { createSelector } from "@ngrx/store";
import { AppState } from "../app.state"

export const selectBlogState = (state: AppState) => state.blogs;

export const blogs = createSelector(
  selectBlogState,
  (state) => state.blogs
)