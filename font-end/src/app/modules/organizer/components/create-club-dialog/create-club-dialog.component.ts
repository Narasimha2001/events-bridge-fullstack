import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ClubService } from 'src/app/core/services/club.service';

@Component({
  selector: 'app-create-club-dialog',
  templateUrl: './create-club-dialog.component.html',
  styleUrls: ['./create-club-dialog.component.scss']
})
export class CreateClubDialogComponent {
  clubForm: FormGroup;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private clubService: ClubService,
    private dialogRef: MatDialogRef<CreateClubDialogComponent>,
    private snackBar: MatSnackBar
  ) {
    this.clubForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.maxLength(500)]]
    });
  }

  onSubmit() {
    if (this.clubForm.invalid) return;

    this.isLoading = true;
    this.clubService.createClub(this.clubForm.value).subscribe({
      next: () => {
        this.isLoading = false;
        this.snackBar.open('Club Created Successfully!', 'Close', { duration: 3000 });
        this.dialogRef.close('success');
      },
      error: (err) => {
        this.isLoading = false;
        this.snackBar.open('Failed to create club', 'Close', { duration: 3000, panelClass: 'error-snackbar' });
      }
    });
  }

  onCancel() {
    this.dialogRef.close();
  }
}