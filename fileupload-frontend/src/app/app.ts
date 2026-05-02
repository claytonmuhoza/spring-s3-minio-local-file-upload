import { Component} from '@angular/core';
import {FileManagerComponent} from './components/file-manager.component/file-manager.component';

@Component({
  selector: 'app-root',
  imports: [FileManagerComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
}
