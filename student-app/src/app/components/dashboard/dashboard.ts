import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { StudentService } from '../../services/student';
import { InternshipService } from '../../services/internship';
import { RecommendationService } from '../../services/recommendation';
import { AuthService } from '../../services/auth';
import { Student } from '../../models/student.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatCardModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
  student: Student | null = null;
  internshipCount = 0;
  recommendationCount = 0;
  applicationCount = 0;

  constructor(
    private studentService: StudentService,
    private internshipService: InternshipService,
    private recommendationService: RecommendationService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const userId = this.authService.getUserId();

    this.studentService.getByUserId(userId).subscribe({
      next: (student) => {
        this.student = student;
        this.cdr.detectChanges();

        this.recommendationService.getByStudentId(student.id).subscribe({
          next: (recs) => {
            this.recommendationCount = recs.length;
            this.cdr.detectChanges();
          },
          error: () => this.recommendationCount = 0
        });

        this.internshipService.getStudentApplications(student.id).subscribe({
          next: (apps) => {
            this.applicationCount = apps.length;
            this.cdr.detectChanges();
          },
          error: () => this.applicationCount = 0
        });
      },
      error: (err) => console.error('Greška pri učitavanju studenta', err)
    });

    this.internshipService.getAll().subscribe({
      next: (page) => {
        this.internshipCount = page.totalElements;
        this.cdr.detectChanges();
      },
      error: () => this.internshipCount = 0
    });
  }
}