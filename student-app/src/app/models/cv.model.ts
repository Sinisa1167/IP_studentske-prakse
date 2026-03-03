export interface CvEntry {
  id?: number;
  type: string; // EDUCATION, EXPERIENCE, INTERNSHIP, SKILL, INTEREST
  title: string;
  description?: string;
  startDate?: string;
  endDate?: string;
  extra?: string;
}