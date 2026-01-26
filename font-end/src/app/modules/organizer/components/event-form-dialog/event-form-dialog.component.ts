import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { EventService } from 'src/app/core/services/event.service';

@Component({
  selector: 'app-event-form-dialog',
  templateUrl: './event-form-dialog.component.html',
  styleUrls: ['./event-form-dialog.component.scss']
})
export class EventFormDialogComponent implements OnInit {
  eventForm: FormGroup;
  isEditMode = false;
  eventId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private eventService: EventService,
    private dialogRef: MatDialogRef<EventFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any // <--- Inject Data
  ) {
    this.eventForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      location: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      capacity: [100, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    if (this.data && this.data.mode === 'edit' && this.data.event) {
      this.isEditMode = true;
      this.eventId = this.data.event.eventId;

      this.eventForm.patchValue({
        title: this.data.event.title,
        description: this.data.event.description,
        location: this.data.event.location,
        startTime: this.data.event.startTime,
        endTime: this.data.event.endTime,
        capacity: this.data.event.capacity
      });
    }
  }

  onSubmit() {
    if (this.eventForm.invalid) return;

    const formValue = this.eventForm.value;
    const payload = { ...formValue, clubId: this.data.clubId, eventId: this.eventId }

    if (this.isEditMode && this.eventId) {
      this.eventService.updateEvent(payload).subscribe({
        next: () => {
          this.dialogRef.close(true);
        },
        error: (err) => console.error(err)
      });
    } else {
      this.eventService.createEvent(payload).subscribe({
        next: () => {
          this.dialogRef.close(true);
        },
        error: (err) => console.error(err)
      });
    }
  }
}