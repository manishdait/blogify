import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { BlogService } from '../../../../service/blog.service';
import { BlogRequest } from '../../../../models/blog';
import { FormsModule } from '@angular/forms';
import { Editor, NgxEditorComponent, NgxEditorMenuComponent } from 'ngx-editor';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../store/app.state';
import { addBlog } from '../../../../store/blog/blog.actions';

@Component({
  selector: 'app-blog-form',
  imports: [NgxEditorComponent, NgxEditorMenuComponent, FormsModule],
  templateUrl: './blog-form.component.html',
  styleUrl: './blog-form.component.css'
})
export class BlogFormComponent implements OnInit {
  @ViewChild('title') title!: ElementRef;

  html = '';
  editor: Editor;

  constructor (private blogService: BlogService, private store: Store<AppState>, private router: Router, private location: Location) {
    this.editor = new Editor();
  }
  
  ngOnInit(): void {}

  publish() {
    console.log(this.html);
    
    var request: BlogRequest = {
      title: this.title.nativeElement.value,
      content: this.html
    }

    this.title.nativeElement.value = '';
    
    this.blogService.createBlog(request).subscribe({
      next: (res) => {
        this.store.dispatch(addBlog({blog: res})); 
        this.router.navigate(['/'], {replaceUrl: true});
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  cancle() {
    this.location.back();
  }
}
