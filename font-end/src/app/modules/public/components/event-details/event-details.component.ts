import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService } from 'src/app/core/services/event.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RegistrationService } from 'src/app/core/services/registration.service';

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.scss']
})
export class EventDetailsComponent implements OnInit {
  event: any;
  isLoading = true;
  isRegistering = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private eventService: EventService,
    public authService: AuthService, 
    private registrationService: RegistrationService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadEvent(Number(id));
    }
  }

  loadEvent(id: number) {
    this.isLoading = true;
    this.eventService.getEventById(id).subscribe({
      next: (data) => {
        this.event = data;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.snackBar.open('Event not found', 'Close');
        this.router.navigate(['/public/events']);
      }
    });
  }

  onRegister() {
    if (!this.authService.isLoggedIn()) {
      this.snackBar.open('Please login to register', 'Login', { duration: 3000 })
        .onAction().subscribe(() => this.router.navigate(['/auth/login']));
      return;
    }

    if (!this.authService.hasRole('STUDENT')) {
      this.snackBar.open('Only students can register for events', 'Close');
      return;
    }

    this.isRegistering = true;
    this.registrationService.registerForEvent(this.event.id).subscribe({
      next: () => {
        this.isRegistering = false;
        this.snackBar.open('Registration Successful! Check your email.', 'OK', { duration: 5000 });
        this.loadEvent(Number(this.route.snapshot.paramMap.get('id')));
      },
      error: (err) => {
        this.isRegistering = false;
        const msg = err.error?.message || 'Registration failed';
        this.snackBar.open(msg, 'Close', { duration: 5000 });
      }
    });
  }

  get fillPercentage(): number {
    if (!this.event?.capacity || !this.event?.registrationCount) return 0;
    return (this.event.registrationCount / this.event.capacity) * 100;
  }
}