import { Routes } from '@angular/router';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./components/login/login').then(m => m.LoginComponent)
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./components/layout/navbar/navbar').then(m => m.Navbar),
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () => import('./components/dashboard/dashboard').then(m => m.Dashboard)
      },
      {
        path: 'internships',
        loadComponent: () => import('./components/internships/internship-list/internship-list').then(m => m.InternshipList)
      },
      {
        path: 'internships/:id',
        loadComponent: () => import('./components/internships/internship-detail/internship-detail').then(m => m.InternshipDetail)
      },
      {
        path: 'cv',
        loadComponent: () => import('./components/cv/cv-editor/cv-editor').then(m => m.CvEditor)
      },
      {
        path: 'recommendations',
        loadComponent: () => import('./components/recommendations/recommendation-list/recommendation-list').then(m => m.RecommendationList)
      },
      {
        path: 'work-diary',
        loadComponent: () => import('./components/work-diary/work-diary-list/work-diary-list').then(m => m.WorkDiaryList)
      },
{
  path: 'applications',
  loadComponent: () => import('./components/applications/application-list/application-list')
    .then(m => m.ApplicationList)
}
    ]
  },
  { path: '**', redirectTo: 'login' }
];