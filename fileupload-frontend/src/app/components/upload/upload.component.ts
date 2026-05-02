import {Component, inject, signal} from '@angular/core';
import {UploadService} from './upload.service';
import {MessageService} from 'primeng/api';
import {FileUploadHandlerEvent, FileUploadModule} from 'primeng/fileupload';
import {FormsModule} from '@angular/forms';
import {ToastModule} from 'primeng/toast';
import {FloatLabelModule} from 'primeng/floatlabel';
import {InputTextModule} from 'primeng/inputtext';
import {InputNumberModule} from 'primeng/inputnumber';

@Component({
  selector: 'app-upload',
  imports: [
    FormsModule,
    ToastModule,
    FileUploadModule,
    InputTextModule,
    InputNumberModule,
    FloatLabelModule],
  providers: [MessageService],
  templateUrl: './upload.component.html',
  styleUrl: './upload.component.css',
})
export class UploadComponent {
  private uploadService = inject(UploadService);
  private messageService = inject(MessageService);

  // État local (Signals)
  projectId = signal<string>('');
  referenceYear = signal<number | undefined>(undefined);
  uploadedFileUri = signal<string | null>(null);

  onUpload(event: FileUploadHandlerEvent){
    const file = event.files[0];

    if (file) {
      this.uploadService.uploadData(file, this.projectId(), this.referenceYear()).subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: response.message
          });
          this.uploadedFileUri.set(response.fileUri);
          event.files = [];
        },
        error: (err) => {
          console.error(err);
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: err.error?.error || 'Échec de téléversement.'
          });
        }
      });
    }
  }
}
