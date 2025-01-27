import { Component, ElementRef, ViewChild } from '@angular/core';
import { BlogService } from '../../../service/blog.service';
import { MiniEditorComponent } from 'mini-editor';
import { BlogRequest } from '../../models/blog';

@Component({
  selector: 'app-blog-form',
  imports: [MiniEditorComponent],
  templateUrl: './blog-form.component.html',
  styleUrl: './blog-form.component.css'
})
export class BlogFormComponent {
  @ViewChild('title') title!: ElementRef;
  @ViewChild('editor') editor!: MiniEditorComponent;

  constructor (private blogService: BlogService) {}

  publish() {
    var request: BlogRequest = {
      title: this.title.nativeElement.value,
      content: this.editor.content
    }

    this.title.nativeElement.value = '';
    this.editor.reset();
    
    this.blogService.createBlog(request).subscribe(
      (res) => {
        console.log(res);
      }
    );
  }
}
