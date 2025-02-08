export interface BlogRequest {
  title: string, 
  content: string, 
}

export interface BlogResponse {
  id: number,
  title: string, 
  content: string, 
  author: string, 
  comments: number,
  created_at: Date,
  edited_at: Date,
  author_img: string | null
}

export interface PageBlogResponse {
  content: BlogResponse[], 
  prev: boolean,
  next: boolean
}