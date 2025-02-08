export interface CommentResponse {
  id: number, 
  message: string, 
  author: string, 
  author_img: string | null
  is_owned: boolean,
  created_at: Date,
  edited_at: Date,
}

export interface CommentRequest {
  blog_id: number, 
  message: string,
}

export interface PageCommentResponse {
  content: CommentResponse[],
  next: boolean,
  prev: boolean
}
