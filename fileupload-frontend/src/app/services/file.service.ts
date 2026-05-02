import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UploadResponse} from '../models/file.model';

@Injectable({
  providedIn: 'root',
})
export class FileService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/v1/files';

  upload(file: File, projectId?: string): Observable<UploadResponse> {
    const formData = new FormData();
    formData.append('file', file);

    if (projectId) {
      formData.append('projectId', projectId);
    }

    return this.http.post<UploadResponse>(this.baseUrl, formData);
  }

  delete(uri: string): Observable<void> {
    const params = new HttpParams().set('uri', uri);
    return this.http.delete<void>(this.baseUrl, { params });
  }

  download(uri: string): void {
    const url = `${this.baseUrl}?uri=${encodeURIComponent(uri)}`;
    window.open(url, '_blank');
  }
}
