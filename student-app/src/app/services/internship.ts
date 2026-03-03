import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Internship, PageResponse } from '../models/internship.model';

@Injectable({ providedIn: 'root' })
export class InternshipService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 10, search?: string, techId?: number): Observable<PageResponse<Internship>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (search) params = params.set('search', search);
    if (techId) params = params.set('techId', techId);

    return this.http.get<PageResponse<Internship>>(`${this.apiUrl}/internships`, { params });
  }

  getById(id: number): Observable<Internship> {
    return this.http.get<Internship>(`${this.apiUrl}/internships/${id}`);
  }

  apply(studentId: number, internshipId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/applications/student/${studentId}/internship/${internshipId}`, {});
  }

  getStudentApplications(studentId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/applications/student/${studentId}`);
  }
}