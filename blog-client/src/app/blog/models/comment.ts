export interface CommentResponse {
  id: number, 
  message: string, 
  author: string, 
  createdAt: Date
}

export interface CommentRequest {
  blogId: number, 
  message: string,
}