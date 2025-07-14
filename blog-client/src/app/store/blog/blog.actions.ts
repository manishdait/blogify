import { createAction, props } from "@ngrx/store";
import { BlogResponse } from "../../models/blog";

export const setBlogs = createAction('Blog [Set Blogs]', props<{blogs: BlogResponse[]}>());
export const addBlog = createAction('Blog [Add Blog]', props<{blog: BlogResponse}>());
export const updateBlog = createAction('Blog [Update Blog]', props<{blog: BlogResponse}>());
export const removeBlog = createAction('Blog [Remove Blog]', props<{id: number}>());
