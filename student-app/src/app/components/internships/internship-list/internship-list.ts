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
import { Router } from '@angular/router';

@Component({
  selector: 'app-internship-list',
  standalone: true,
  imports: [
    CommonModule,
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
  allInternships: Internship[] = [];
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  searchTerm = '';
  selectedTechId: number | null = null;
  selectedCompanyId: number | null = null;
  technologies: Technology[] = [];
  companies: { id: number; name: string }[] = [];
  loading = false;
  displayedColumns = ['title', 'company', 'technologies', 'startDate', 'endDate'];

  constructor(
    private internshipService: InternshipService,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    public router: Router
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
  this.internshipService.getAll(0, 1000, undefined, undefined).subscribe({
    next: (page) => {
      this.allInternships = page.content;

      // Izvuci unikatne kompanije iz praksi
      const companyMap = new Map<number, string>();
      page.content.forEach((i: Internship) => {
        if (i.company?.id) companyMap.set(i.company.id, i.company.name);
      });
      this.companies = Array.from(companyMap.entries())
        .map(([id, name]) => ({ id, name }));

      this.applyFilters();
      this.loading = false;
      this.cdr.detectChanges();
    },
    error: () => this.loading = false
  });
}

  applyFilters(): void {
  let filtered = [...this.allInternships];

  // Pretraga po nazivu
  if (this.searchTerm?.trim()) {
    const term = this.searchTerm.toLowerCase();
    filtered = filtered.filter(i =>
      i.title.toLowerCase().includes(term) ||
      i.company?.name.toLowerCase().includes(term) ||
      i.technologies?.some(t => t.name.toLowerCase().includes(term))
    );
  }

  // Filter po tehnologiji
  if (this.selectedTechId) {
    filtered = filtered.filter(i =>
      i.technologies?.some(t => t.id === this.selectedTechId)
    );
  }

  // Filter po kompaniji
  if (this.selectedCompanyId) {
    filtered = filtered.filter(i => i.company?.id === this.selectedCompanyId);
  }

  this.totalElements = filtered.length;
  const start = this.pageIndex * this.pageSize;
  this.internships = filtered.slice(start, start + this.pageSize);
  this.cdr.detectChanges();
}

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.applyFilters();
  }

  onSearch(): void {
  this.pageIndex = 0;
  this.applyFilters();
}

  onTechFilter(): void {
    this.pageIndex = 0;
    this.loadInternships();
  }

  onCompanyFilter(): void {
    this.pageIndex = 0;
    this.applyFilters();
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.selectedTechId = null;
    this.selectedCompanyId = null;
    this.pageIndex = 0;
    this.loadInternships();
  }
}