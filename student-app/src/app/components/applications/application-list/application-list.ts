import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { InternshipService } from '../../../services/internship';
import { StudentService } from '../../../services/student';
import { AuthService } from '../../../services/auth';

@Component({
  selector: 'app-application-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatDividerModule
  ],
  templateUrl: './application-list.html',
  styleUrl: './application-list.scss'
})
export class ApplicationList implements OnInit {
  applications: any[] = [];
  loading = false;

  constructor(
    private internshipService: InternshipService,
    private studentService: StudentService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const userId = this.authService.getUserId();
    this.studentService.getByUserId(userId).subscribe({
      next: (student) => {
        this.loading = true;
        this.internshipService.getStudentApplications(student.id).subscribe({
          next: (apps) => {
            this.applications = apps;
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: () => this.loading = false
        });
      }
    });
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'ACCEPTED': return '#2e7d32';
      case 'REJECTED': return '#c62828';
      default: return '#e65100';
    }
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'ACCEPTED': return 'Prihvaćena';
      case 'REJECTED': return 'Odbijena';
      default: return 'Na čekanju';
    }
  }

  getStatusIcon(status: string): string {
    switch (status) {
      case 'ACCEPTED': return 'check_circle';
      case 'REJECTED': return 'cancel';
      default: return 'hourglass_empty';
    }
  }
}