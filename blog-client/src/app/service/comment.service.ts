import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CommentResponse, CommentRequest, PageCommentResponse } from '../models/comment';
import { environment } from '../../environments/environment';

const URL = `${environment.API_DOMAIN}/comment`;

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  constructor(private client: HttpClient) { }

  fetchCommentsForBlog(blogId: number): Observable<PageCommentResponse> {
    return this.client.get<PageCommentResponse>(`${URL}/blog/${blogId}`);
  }
  
  createComment(request: CommentRequest): Observable<CommentResponse> {
    return this.client.post<CommentResponse>(`${URL}`, request);
  }

  updateComment(commentId: number, request: CommentRequest): Observable<CommentResponse> {
    return this.client.put<CommentResponse>(`${URL}/${commentId}`, request);
  }

  deleteComment(commentId: number): Observable<void> {
    return this.client.delete<void>(`${URL}/${commentId}`);
  }
}
