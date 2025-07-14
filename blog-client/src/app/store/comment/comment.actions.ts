import { createAction, props } from "@ngrx/store";
import { CommentResponse } from "../../models/comment";

export const setComments = createAction('[Set Comments] Comment', props<{comments: CommentResponse[]}>());
export const addComment = createAction('[Add Comments] Comment', props<{comment: CommentResponse}>());
export const updateComment = createAction('[Update Comments] Comments', props<{comment: CommentResponse}>());
export const removeComment = createAction('[Remove Comments] Comment', props<{id: number}>());