import {Component, inject, signal} from '@angular/core';
import {NonNullableFormBuilder, ReactiveFormsModule} from '@angular/forms';
import {ButtonModule} from 'primeng/button';
import {InputTextModule} from 'primeng/inputtext';
import {MessageModule} from 'primeng/message';
import {ProgressSpinnerModule} from 'primeng/progressspinner';
import {HttpErrorResponse} from '@angular/common/http';
import {UploadResponse} from '../../models/file.model';
import {FileService} from '../../services/file.service';

@Component({
  selector: 'app-file-manager',
  imports: [ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    MessageModule,
    ProgressSpinnerModule],
  providers: [],
  templateUrl: './file-manager.component.html',
  styleUrl: './file-manager.component.css',
})
export class FileManagerComponent {
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly fileService = inject(FileService);

  readonly uploadForm = this.fb.group({
    projectId: [''],
  });

  readonly selectedFile = signal<File | null>(null);
  readonly isUploading = signal<boolean>(false);
  readonly isDeleting = signal<boolean>(false);
  readonly errorMessage = signal<string | null>(null);
  readonly uploadedResult = signal<UploadResponse | null>(null);

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile.set(input.files[0]);
      this.errorMessage.set(null);
    }
  }

  onSubmit(): void {
    const file = this.selectedFile();
    if (!file) return;

    this.isUploading.set(true);
    this.errorMessage.set(null);
    this.uploadedResult.set(null);

    const formValues = this.uploadForm.getRawValue();

    this.fileService.upload(file, formValues.projectId)
      .subscribe({
        next: (response) => {
          this.uploadedResult.set(response);
          this.isUploading.set(false);
          this.uploadForm.reset();
          this.selectedFile.set(null);
        },
        error: (err: HttpErrorResponse) => {
          this.errorMessage.set(err.error?.message || 'An error occurred during the upload.');
          this.isUploading.set(false);
        }
      });
  }

  onDownload(uri: string): void {
    this.fileService.download(uri);
  }

  onDelete(uri: string): void {
    this.isDeleting.set(true);
    this.errorMessage.set(null);

    this.fileService.delete(uri).subscribe({
      next: () => {
        this.uploadedResult.set(null);
        this.isDeleting.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage.set('Échec de la suppression : ' + (err.error?.message || err.message));
        this.isDeleting.set(false);
      }
    });
  }
}
