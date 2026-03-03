import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { InternshipService } from '../../../services/internship';
import { Internship, Technology } from '../../../models/internship.model';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-internship-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatSelectModule
  ],
  templateUrl: './internship-list.html',
  styleUrl: './internship-list.scss'
})
export class InternshipList implements OnInit {
  internships: Internship[] = [];
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  searchTerm = '';
  selectedTechId: number | null = null;
  technologies: Technology[] = [];
  loading = false;

  displayedColumns = ['title', 'company', 'technologies', 'startDate', 'endDate', 'actions'];

  constructor(
    private internshipService: InternshipService,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadTechnologies();
    this.loadInternships();
  }

  loadTechnologies(): void {
    this.http.get<Technology[]>('http://localhost:8080/api/technologies').subscribe({
      next: (techs) => this.technologies = techs
    });
  }

  loadInternships(): void {
    this.loading = true;
    this.internshipService.getAll(
      this.pageIndex,
      this.pageSize,
      this.searchTerm || undefined,
      this.selectedTechId || undefined
    ).subscribe({
      next: (page) => {
        this.internships = page.content;
        this.totalElements = page.totalElements;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => this.loading = false
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadInternships();
  }

  onSearch(): void {
    this.pageIndex = 0;
    this.loadInternships();
  }

  onTechFilter(): void {
    this.pageIndex = 0;
    this.loadInternships();
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.selectedTechId = null;
    this.pageIndex = 0;
    this.loadInternships();
  }
}