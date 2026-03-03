export interface Technology {
  id: number;
  name: string;
}

export interface Company {
  id: number;
  name: string;
  address?: string;
  contactEmail?: string;
  description?: string;
}

export interface Internship {
  id: number;
  title: string;
  description?: string;
  startDate?: string;
  endDate?: string;
  conditions?: string;
  active: boolean;
  company: Company;
  technologies: Technology[];
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}