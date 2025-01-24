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
  createdAt: Date
}