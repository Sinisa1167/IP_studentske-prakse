import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WorkDiary } from '../models/work-diary.model';

@Injectable({ providedIn: 'root' })
export class WorkDiaryService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getEntries(studentId: number, internshipId: number): Observable<WorkDiary[]> {
    return this.http.get<WorkDiary[]>(`${this.apiUrl}/diary/student/${studentId}/internship/${internshipId}`);
  }

  create(studentId: number, internshipId: number, entry: WorkDiary): Observable<WorkDiary> {
    return this.http.post<WorkDiary>(`${this.apiUrl}/diary/student/${studentId}/internship/${internshipId}`, entry);
  }

  update(id: number, entry: WorkDiary): Observable<WorkDiary> {
    return this.http.put<WorkDiary>(`${this.apiUrl}/diary/${id}`, entry);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/diary/${id}`);
  }
}