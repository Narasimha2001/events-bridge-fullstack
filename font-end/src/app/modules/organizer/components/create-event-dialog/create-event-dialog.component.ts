import { Component, Inject } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EventService } from 'src/app/core/services/event.service';

@Component({
  selector: 'app-create-event-dialog',
  templateUrl: './create-event-dialog.component.html',
  styleUrls: ['./create-event-dialog.component.scss']
})
export class CreateEventDialogComponent {
  eventForm: FormGroup;
  isLoading = false;
  isEditMode = false; 

  constructor(
    private fb: FormBuilder,
    private eventService: EventService,
    private dialogRef: MatDialogRef<CreateEventDialogComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.eventForm = new FormGroup(
      {
        title: new FormControl('', Validators.required),
        description: new FormControl('', Validators.required),
        location: new FormControl('', Validators.required),
        startTime: new FormControl('', Validators.required),
        endTime: new FormControl('', Validators.required),
        capacity: new FormControl(0, Validators.required),
        clubId: new FormControl(this.data.clubId, Validators.required)
      }
    );
  }

  toLocalISOString(date: Date | null): string | null {
    if (!date) return null;
    const copy = new Date(date);
    copy.setMinutes(date.getMinutes() - date.getTimezoneOffset());
    return copy.toISOString();
  }

  ngOnInit(): void {
    if (this.isEditMode) {
      this.eventForm.patchValue({
        title: this.data.title,
        description: this.data.description,
        location: this.data.location,
        capacity: this.data.capacity,
        startTime: new Date(this.data.startTime), 
        endTime: new Date(this.data.endTime)
      });
    }
  }

  onSubmit() {
    if (this.eventForm.invalid) return;

    this.eventForm.controls['startTime'].setValue(this.toLocalISOString(this.eventForm.controls['startTime'].value))

    this.eventForm.controls['endTime'].setValue(this.toLocalISOString(this.eventForm.controls['endTime'].value))

    const start = new Date(this.eventForm.value.startTime);
    const end = new Date(this.eventForm.value.endTime);

    if (end <= start) {
      this.snackBar.open('End time must be after start time', 'Close', { duration: 3000 });
      return;
    }

    this.isLoading = true;

    const payload = {...this.eventForm.value, clubId: this.data.clubId};
    this.eventService.createEvent(payload).subscribe({
      next: () => {
        this.isLoading = false;
        this.snackBar.open('Event Created Successfully!', 'Close', { duration: 3000 });
        this.dialogRef.close('success');
      },
      error: (err) => {
        this.isLoading = false;
        this.snackBar.open('Failed to create event', 'Close', { duration: 3000 });
      }
    });
  }

  onCancel() {
    this.dialogRef.close();
  }
}