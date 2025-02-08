import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BlogRequest, BlogResponse, PageBlogResponse } from '../models/blog';
import { environment } from '../../environments/environment';

const URL = `${environment.API_DOMAIN}/blog`;

@Injectable({
  providedIn: 'root'
})
export class BlogService {
  constructor(private client: HttpClient) {}
  
  fetchBlogs(): Observable<PageBlogResponse> {
    return this.client.get<PageBlogResponse>(`${URL}`);
  }

  fetchBlog(id: number): Observable<BlogResponse> {
    return this.client.get<BlogResponse>(`${URL}/${id}`);
  }

  createBlog(request: BlogRequest): Observable<BlogResponse> {
    return this.client.post<BlogResponse>(`${URL}`, request);
  }
}
