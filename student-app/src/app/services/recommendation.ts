import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AiRecommendation } from '../models/recommendation.model';

@Injectable({ providedIn: 'root' })
export class RecommendationService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getByStudentId(studentId: number): Observable<AiRecommendation[]> {
    return this.http.get<AiRecommendation[]>(`${this.apiUrl}/recommendations/student/${studentId}`);
  }

  generate(studentId: number): Observable<AiRecommendation[]> {
    return this.http.post<AiRecommendation[]>(`${this.apiUrl}/recommendations/student/${studentId}/generate`, {});
  }
}