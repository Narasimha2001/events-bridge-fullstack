import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RegistrationService } from 'src/app/core/services/registration.service';

@Component({
  selector: 'app-attendees-dialog',
  templateUrl: './attendees-dialog.component.html',
  styleUrls: ['./attendees-dialog.component.scss']
})
export class AttendeesDialogComponent implements OnInit {
  attendees: any[] = [];
  isLoading = true;

  constructor(
    private regService: RegistrationService,
    public dialogRef: MatDialogRef<AttendeesDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { eventId: number, eventTitle: string }
  ) { }

  ngOnInit(): void {
    this.loadAttendees();
  }

  loadAttendees() {
    this.isLoading = true;
    this.regService.getEventAttendees(this.data.eventId).subscribe({
      next: (data) => {
        this.attendees = data;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  downloadCsv() {
    if (this.attendees.length === 0) return;

    const headers = ['Name', 'Email', 'Student ID', 'Registered At', 'Status'];

    const rows = this.attendees.map(user => [
      `"${user.studentName}"`,
      user.studentEmail,
      user.studentId || 'N/A',
      `"${new Date(user.registeredAt).toLocaleString()}"`,
      user.status
    ]);

    const csvContent = [
      headers.join(','),
      ...rows.map(row => row.join(','))
    ].join('\n');

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.setAttribute('href', url);
    link.setAttribute('download', `attendees_${this.data.eventTitle}_${new Date().toISOString().slice(0, 10)}.csv`);
    link.style.visibility = 'hidden';

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}