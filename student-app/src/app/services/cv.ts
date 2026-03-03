import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CvEntry } from '../models/cv.model';

@Injectable({ providedIn: 'root' })
export class CvService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getByStudentId(studentId: number): Observable<CvEntry[]> {
    return this.http.get<CvEntry[]>(`${this.apiUrl}/cv/student/${studentId}`);
  }

  create(studentId: number, entry: CvEntry): Observable<CvEntry> {
    return this.http.post<CvEntry>(`${this.apiUrl}/cv/student/${studentId}`, entry);
  }

  update(id: number, entry: CvEntry): Observable<CvEntry> {
    return this.http.put<CvEntry>(`${this.apiUrl}/cv/${id}`, entry);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/cv/${id}`);
  }
}