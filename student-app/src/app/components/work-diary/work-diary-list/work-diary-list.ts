import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import { WorkDiaryService } from '../../../services/work-diary';
import { InternshipService } from '../../../services/internship';
import { StudentService } from '../../../services/student';
import { AuthService } from '../../../services/auth';
import { WorkDiary } from '../../../models/work-diary.model';

@Component({
  selector: 'app-work-diary-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatDividerModule
  ],
  templateUrl: './work-diary-list.html',
  styleUrl: './work-diary-list.scss'
})
export class WorkDiaryList implements OnInit {
  studentId: number | null = null;
  applications: any[] = [];
  selectedInternshipId: number | null = null;
  selectedInternshipTitle = '';
  entries: WorkDiary[] = [];
  loading = false;
  showForm = false;
  editingEntry: WorkDiary | null = null;

  entryForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private workDiaryService: WorkDiaryService,
    private internshipService: InternshipService,
    private studentService: StudentService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {
    this.entryForm = this.fb.group({
      weekNumber: ['', [Validators.required, Validators.min(1)]],
      activities: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const userId = this.authService.getUserId();
    this.studentService.getByUserId(userId).subscribe({
      next: (student) => {
        this.studentId = student.id;
        this.loadApplications();
      }
    });
  }

  loadApplications(): void {
    if (!this.studentId) return;
    this.internshipService.getStudentApplications(this.studentId).subscribe({
      next: (apps) => {
        this.applications = apps.filter(a => a.status === 'ACCEPTED');
        this.cdr.detectChanges();
      }
    });
  }

  selectInternship(app: any): void {
    this.selectedInternshipId = app.internship.id;
    this.selectedInternshipTitle = app.internship.title;
    this.loadEntries();
  }

  loadEntries(): void {
    if (!this.studentId || !this.selectedInternshipId) return;
    this.loading = true;
    this.workDiaryService.getEntries(this.studentId, this.selectedInternshipId).subscribe({
      next: (entries) => {
        this.entries = entries;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => this.loading = false
    });
  }

  openForm(entry?: WorkDiary): void {
    this.editingEntry = entry || null;
    if (entry) {
      this.entryForm.patchValue(entry);
    } else {
      this.entryForm.reset();
    }
    this.showForm = true;
    this.cdr.detectChanges();
  }

  closeForm(): void {
    this.showForm = false;
    this.editingEntry = null;
    this.entryForm.reset();
  }

  save(): void {
    if (this.entryForm.invalid || !this.studentId || !this.selectedInternshipId) return;

    const entry: WorkDiary = this.entryForm.value;

    if (this.editingEntry?.id) {
      this.workDiaryService.update(this.editingEntry.id, entry).subscribe({
        next: () => {
          this.snackBar.open('Unos ažuriran!', 'OK', { duration: 2000 });
          this.closeForm();
          this.loadEntries();
        },
        error: () => this.snackBar.open('Greška pri ažuriranju.', 'OK', { duration: 2000 })
      });
    } else {
      this.workDiaryService.create(this.studentId, this.selectedInternshipId, entry).subscribe({
        next: () => {
          this.snackBar.open('Unos dodan!', 'OK', { duration: 2000 });
          this.closeForm();
          this.loadEntries();
        },
        error: () => this.snackBar.open('Unos za ovu sedmicu već postoji.', 'OK', { duration: 3000 })
      });
    }
  }

  delete(id: number): void {
    if (!confirm('Da li ste sigurni?')) return;
    this.workDiaryService.delete(id).subscribe({
      next: () => {
        this.snackBar.open('Unos obrisan!', 'OK', { duration: 2000 });
        this.loadEntries();
      }
    });
  }
}