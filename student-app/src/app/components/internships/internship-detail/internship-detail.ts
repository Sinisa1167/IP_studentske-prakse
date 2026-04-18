import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Location } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { InternshipService } from '../../../services/internship';
import { AuthService } from '../../../services/auth';
import { StudentService } from '../../../services/student';
import { Internship } from '../../../models/internship.model';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-internship-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDividerModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './internship-detail.html',
  styleUrl: './internship-detail.scss'
})
export class InternshipDetail implements OnInit {
  internship: Internship | null = null;
  studentId: number | null = null;
  alreadyApplied = false;
  applying = false;

  constructor(
    private route: ActivatedRoute,
    private internshipService: InternshipService,
    private studentService: StudentService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef,
    private location: Location
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    const userId = this.authService.getUserId();
    this.studentService.getByUserId(userId).subscribe({
      next: (student) => {
        this.studentId = student.id;
        this.internshipService.getStudentApplications(student.id).subscribe({
          next: (apps) => {
            this.alreadyApplied = apps.some(a => a.internship?.id === id);
            this.cdr.detectChanges();
          }
        });
      }
    });
    this.internshipService.getById(id).subscribe({
      next: (internship) => {
        this.internship = internship;
        this.cdr.detectChanges();
      }
    });
  }

  goBack(): void {
    this.location.back();
  }

  apply(): void {
    if (!this.studentId || !this.internship) return;
    this.applying = true;
    this.internshipService.apply(this.studentId, this.internship.id).subscribe({
      next: () => {
        this.alreadyApplied = true;
        this.applying = false;
        this.snackBar.open('Uspješno ste se prijavili na praksu!', 'OK', { duration: 3000 });
        this.cdr.detectChanges();
      },
      error: () => {
        this.applying = false;
        this.snackBar.open('Greška pri prijavi.', 'OK', { duration: 3000 });
      }
    });
  }
}