import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Student } from '../models/student.model';
import { PageResponse } from '../models/internship.model';

@Injectable({ providedIn: 'root' })
export class StudentService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getById(id: number): Observable<Student> {
    return this.http.get<Student>(`${this.apiUrl}/students/${id}`);
  }

  getByUserId(userId: number): Observable<Student> {
    return this.http.get<Student>(`${this.apiUrl}/students/user/${userId}`);
  }

  update(id: number, student: Student): Observable<Student> {
    return this.http.put<Student>(`${this.apiUrl}/students/${id}`, student);
  }

  uploadPhoto(id: number, file: File): Observable<string> {
  const formData = new FormData();
  formData.append('file', file);
  return this.http.post(`${this.apiUrl}/students/${id}/photo`, formData, { responseType: 'text' });
}
}