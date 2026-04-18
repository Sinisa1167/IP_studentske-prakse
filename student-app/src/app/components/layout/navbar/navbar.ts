import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet, Router } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss'
})
export class Navbar implements OnInit {
  username = '';
  sidenavExpanded = true;

  navItems = [
    { path: '/dashboard', icon: 'dashboard', label: 'Dashboard', exact: true },
    { path: '/dashboard/internships', icon: 'work', label: 'Prakse', exact: false },
    { path: '/dashboard/cv', icon: 'description', label: 'Moj CV', exact: false },
    { path: '/dashboard/recommendations', icon: 'psychology', label: 'AI Preporuke', exact: false },
    { path: '/dashboard/work-diary', icon: 'book', label: 'Dnevnik rada', exact: false },
    { path: '/dashboard/applications', icon: 'assignment', label: 'Moje prijave', exact: false }
  ];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.username = localStorage.getItem('username') || '';
  }

  toggleSidenav(): void {
    this.sidenavExpanded = !this.sidenavExpanded;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}