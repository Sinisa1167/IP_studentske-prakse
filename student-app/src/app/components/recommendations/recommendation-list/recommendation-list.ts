import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterLink } from '@angular/router';
import { RecommendationService } from '../../../services/recommendation';
import { StudentService } from '../../../services/student';
import { AuthService } from '../../../services/auth';
import { AiRecommendation } from '../../../models/recommendation.model';

@Component({
  selector: 'app-recommendation-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './recommendation-list.html',
  styleUrl: './recommendation-list.scss'
})
export class RecommendationList implements OnInit {
  recommendations: AiRecommendation[] = [];
  studentId: number | null = null;
  loading = false;
  generating = false;

  constructor(
    private recommendationService: RecommendationService,
    private studentService: StudentService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const userId = this.authService.getUserId();
    this.studentService.getByUserId(userId).subscribe({
      next: (student) => {
        this.studentId = student.id;
        this.loadRecommendations();
      }
    });
  }

  loadRecommendations(): void {
    if (!this.studentId) return;
    this.loading = true;
    this.recommendationService.getByStudentId(this.studentId).subscribe({
      next: (recs) => {
        this.recommendations = recs;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => this.loading = false
    });
  }

  generate(): void {
  if (!this.studentId) return;
  this.generating = true;
  this.cdr.detectChanges();
  this.snackBar.open('Generišem preporuke, molimo sačekajte...', '', { duration: 10000 });

  this.recommendationService.generate(this.studentId).subscribe({
    next: (recs) => {
      this.recommendations = recs;
      this.generating = false;
      this.snackBar.dismiss();
      this.snackBar.open('Preporuke su uspješno generisane!', 'OK', { duration: 3000 });
      this.cdr.detectChanges();
    },
    error: (err) => {
      console.log('Greška:', err);
      this.generating = false;
      this.snackBar.dismiss();
      this.snackBar.open('Greška pri generisanju: ' + err.error, 'OK', { duration: 5000 });
      this.cdr.detectChanges();
    }
  });
}

  getScoreColor(score: number): string {
    if (score >= 0.7) return '#2e7d32';
    if (score >= 0.4) return '#e65100';
    return '#c62828';
  }

  getScoreLabel(score: number): string {
    if (score >= 0.7) return 'Visoka podudarnost';
    if (score >= 0.4) return 'Srednja podudarnost';
    return 'Niska podudarnost';
  }

  getScorePercent(score: number): number {
    return Math.round(score * 100);
  }
}