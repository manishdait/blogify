import { createReducer, on } from "@ngrx/store";
import { BlogResponse } from "../../models/blog";
import { addBlog, removeBlog, setBlogs, updateBlog } from "./blog.actions";

export interface BlogState {
  blogs: BlogResponse[]
}

export const initialState: BlogState = {
  blogs: []
}

export const blogReducer = createReducer(
  initialState,
  on(setBlogs, (state, {blogs}) => ({
    ...state,
    blogs: blogs
  })),
  on(addBlog, (state, {blog}) => ({
    ...state,
    blogs: [blog, ...state.blogs]
  })),
  on(updateBlog, (state, {blog}) => ({
    ...state,
    blogs: state.blogs.map(b => b.id === blog.id? blog : b)
  })),
  on(removeBlog, (state, {id}) => ({
    ...state,
    blogs: state.blogs.filter(b => b.id !== id)
  }))
)