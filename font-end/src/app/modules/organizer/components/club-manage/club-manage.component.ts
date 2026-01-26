import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EventService } from 'src/app/core/services/event.service';
import { MatDialog } from '@angular/material/dialog';
import { CreateEventDialogComponent } from '../create-event-dialog/create-event-dialog.component';
import { AttendeesDialogComponent } from '../attendees-dialog/attendees-dialog.component';
import { EventFormDialogComponent } from '../event-form-dialog/event-form-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-club-manage',
  templateUrl: './club-manage.component.html',
  styleUrls: ['./club-manage.component.scss']
})
export class ClubManageComponent implements OnInit {
  clubId!: number;
  events: any[] = [];
  isLoading = true;

  constructor(
    private route: ActivatedRoute,
    private eventService: EventService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  loadClubEvents() {
    if (!this.clubId) return;

    this.isLoading = true;

    this.eventService.getEventsByClub(this.clubId).subscribe({
      next: (data) => {
        this.events = data.content;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  ngOnInit(): void {
    this.clubId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.clubId) {
      this.loadClubEvents();
    }
  }

  loadEvents() {
    this.isLoading = true;
    this.eventService.getEventsByClub(this.clubId).subscribe({
      next: (res) => {
        this.events = res.content;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  openCreateEventDialog() {
    const dialogRef = this.dialog.open(CreateEventDialogComponent, {
      width: 'auto',
      height: 'auto',
      maxHeight: '90vh',
      panelClass: 'modern-dialog-panel',
      autoFocus: false,
      data: { clubId: this.clubId },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'success') {
        this.loadEvents();
      }
    });
  }

  openAttendeesDialog(event: any) {
    this.dialog.open(AttendeesDialogComponent, {
      width: '600px',
      data: { eventId: event.eventId, eventTitle: event.title }
    });
  }

  onDeleteEvent(event: any) {
    if (confirm(`Are you sure you want to cancel "${event.title}"? \nAll registered students will be notified.`)) {
      this.eventService.deleteEvent(event.eventId).subscribe({
        next: () => {
          this.snackBar.open('Event cancelled successfully', 'Close', { duration: 3000 });
          this.loadClubEvents();
        },
        error: () => this.snackBar.open('Failed to cancel event', 'Close')
      });
    }
  }

  onEditEvent(event: any) {
    const dialogRef = this.dialog.open(EventFormDialogComponent, {
      width: '600px',
      data: { mode: 'edit', event: event, clubId: this.clubId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadClubEvents();
      }
    });
  }

  onCreateEvent() {
    const dialogRef = this.dialog.open(CreateEventDialogComponent, {
      width: '600px',
      data: { clubId: this.clubId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'success') {
        this.loadClubEvents();
      }
    });
  }

}