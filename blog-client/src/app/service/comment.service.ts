import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CommentResponse, CommentRequest } from '../blog/models/comment';
import { environment } from '../../environments/environment.development';

const URL = `${environment.API_DOMAIN}/comment`;

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  constructor(private client: HttpClient) { }

  fetchCommentsForBlog(blogId: number): Observable<CommentResponse[]> {
    return this.client.get<CommentResponse[]>(`${URL}/blog/${blogId}`);
  }
  
  createComment(request: CommentRequest): Observable<CommentResponse> {
    return this.client.post<CommentResponse>(`${URL}`, request);
  }
}
