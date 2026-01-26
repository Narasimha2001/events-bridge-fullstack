import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  user: any = null;
  isLoading = true;

  constructor(public authService: AuthService) {}

  ngOnInit(): void {
    this.authService.getProfile().subscribe({
      next: (data) => {
        this.user = data;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  getRoleBadgeColor(role: string): string {
    return role === 'ORGANIZER' ? 'purple-badge' : 'blue-badge';
  }
}