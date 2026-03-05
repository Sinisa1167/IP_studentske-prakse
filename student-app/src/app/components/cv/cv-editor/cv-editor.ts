import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTabsModule } from '@angular/material/tabs';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CvService } from '../../../services/cv';
import { StudentService } from '../../../services/student';
import { AuthService } from '../../../services/auth';
import { CvEntry } from '../../../models/cv.model';
import { Student } from '../../../models/student.model';

@Component({
  selector: 'app-cv-editor',
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
    MatDatepickerModule,
    MatNativeDateModule,
    MatDividerModule,
    MatSnackBarModule,
    MatTabsModule,
    MatChipsModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './cv-editor.html',
  styleUrl: './cv-editor.scss'
})
export class CvEditor implements OnInit {
  student: Student | null = null;
  studentId: number | null = null;
  cvEntries: CvEntry[] = [];
  loading = false;
  showForm = false;
  editingEntry: CvEntry | null = null;
  photoUrl: string | null = null;

  entryForm: FormGroup;

  types = [
    { value: 'EDUCATION', label: 'Obrazovanje' },
    { value: 'EXPERIENCE', label: 'Radno iskustvo' },
    { value: 'INTERNSHIP', label: 'Praksa' },
    { value: 'SKILL', label: 'Vještina' },
    { value: 'INTEREST', label: 'Interesovanje' }
  ];

  skillLevels = [
    { value: 'BEGINNER', label: 'Početnik' },
    { value: 'INTERMEDIATE', label: 'Srednji nivo' },
    { value: 'ADVANCED', label: 'Napredni' }
  ];

  constructor(
    private fb: FormBuilder,
    private cvService: CvService,
    private studentService: StudentService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {
    this.entryForm = this.fb.group({
      type: ['', Validators.required],
      title: ['', Validators.required],
      description: [''],
      startDate: [''],
      endDate: [''],
      extra: ['']
    });
  }

  ngOnInit(): void {
    const userId = this.authService.getUserId();
    this.studentService.getByUserId(userId).subscribe({
      next: (student) => {
        this.student = student;
        this.studentId = student.id;
        if (student.photoPath) {
          this.photoUrl = 'http://localhost:8080/' + student.photoPath;
        }
        this.loadCvEntries();
      }
    });
  }

  loadCvEntries(): void {
    if (!this.studentId) return;
    this.loading = true;
    this.cvService.getByStudentId(this.studentId).subscribe({
      next: (entries) => {
        this.cvEntries = entries;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => this.loading = false
    });
  }

  getEntriesByType(type: string): CvEntry[] {
    return this.cvEntries.filter(e => e.type === type);
  }

  openForm(entry?: CvEntry): void {
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
    if (this.entryForm.invalid || !this.studentId) return;

    const entry: CvEntry = this.entryForm.value;

    if (this.editingEntry?.id) {
      this.cvService.update(this.editingEntry.id, entry).subscribe({
        next: () => {
          this.snackBar.open('Stavka ažurirana!', 'OK', { duration: 2000 });
          this.closeForm();
          this.loadCvEntries();
        }
      });
    } else {
      this.cvService.create(this.studentId, entry).subscribe({
        next: () => {
          this.snackBar.open('Stavka dodana!', 'OK', { duration: 2000 });
          this.closeForm();
          this.loadCvEntries();
        }
      });
    }
  }

  delete(id: number): void {
    if (!confirm('Da li ste sigurni da želite obrisati ovu stavku?')) return;
    this.cvService.delete(id).subscribe({
      next: () => {
        this.snackBar.open('Stavka obrisana!', 'OK', { duration: 2000 });
        this.loadCvEntries();
      }
    });
  }

  onPhotoSelected(event: any): void {
    const file = event.target.files[0];
    if (!file || !this.studentId) return;

    const reader = new FileReader();
    reader.onload = (e) => {
      this.photoUrl = e.target?.result as string;
      this.cdr.detectChanges();
    };
    reader.readAsDataURL(file);

    this.studentService.uploadPhoto(this.studentId, file).subscribe({
      next: () => this.snackBar.open('Slika uspješno uploadovana!', 'OK', { duration: 2000 }),
      error: () => this.snackBar.open('Greška pri uploadu slike.', 'OK', { duration: 2000 })
    });
  }

  getTypeLabel(type: string): string {
    return this.types.find(t => t.value === type)?.label || type;
  }

  getSkillLevelLabel(level: string): string {
    return this.skillLevels.find(l => l.value === level)?.label || level;
  }

  isSkill(): boolean {
    return this.entryForm.get('type')?.value === 'SKILL';
  }

  hasDate(): boolean {
    const type = this.entryForm.get('type')?.value;
    return ['EDUCATION', 'EXPERIENCE', 'INTERNSHIP'].includes(type);
  }
}