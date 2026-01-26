import { Component, inject } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  authService = inject(AuthService);

  getDashboardRoute(): string {
    const role = this.authService.getCurrentUser()?.role;
    return role === 'ORGANIZER' ? '/organizer/dashboard' : '/public/my-registrations';
  }

}
