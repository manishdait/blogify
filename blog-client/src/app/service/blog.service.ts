import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BlogRequest, BlogResponse } from '../blog/models/blog';
import { environment } from '../../environments/environment.development';

const URL = `${environment.API_DOMAIN}/blog`;

@Injectable({
  providedIn: 'root'
})
export class BlogService {
  constructor(private client: HttpClient) {}
  
  fetchBlogs(): Observable<BlogResponse[]> {
    return this.client.get<BlogResponse[]>(`${URL}`);
  }

  fetchBlog(id: number): Observable<BlogResponse> {
    return this.client.get<BlogResponse>(`${URL}/${id}`);
  }

  createBlog(request: BlogRequest): Observable<BlogResponse> {
    return this.client.post<BlogResponse>(`${URL}`, request);
  }
}
