import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UploadService {
  private http = inject(HttpClient);

  uploadData(file: File, projectId?: string, referenceYear?: number): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    if (projectId) {
      formData.append('projectId', projectId);
    }
    if (referenceYear) {
      formData.append('referenceYear', referenceYear.toString());
    }
    return this.http.post('/api/v1/files/upload', formData);
  }
}
