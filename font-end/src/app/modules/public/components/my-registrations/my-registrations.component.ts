import { Component, OnInit } from '@angular/core';
import { RegistrationService } from 'src/app/core/services/registration.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-my-registrations',
  templateUrl: './my-registrations.component.html',
  styleUrls: ['./my-registrations.component.scss']
})
export class MyRegistrationsComponent implements OnInit {
  registrations: any[] = [];
  isLoading = true;

  constructor(
    private regService: RegistrationService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadRegistrations();
  }

  loadRegistrations() {
    this.isLoading = true;
    this.regService.getMyRegistrations().subscribe({
      next: (res) => {
        this.registrations = res.content;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  cancelTicket(regId: number) {
    if (!confirm('Are you sure you want to cancel this ticket? This cannot be undone.')) return;

    this.regService.cancelRegistration(regId).subscribe({
      next: () => {
        this.snackBar.open('Ticket cancelled successfully', 'Close', { duration: 3000 });
        this.loadRegistrations();
      },
      error: () => this.snackBar.open('Failed to cancel ticket', 'Close')
    });
  }
}