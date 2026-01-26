import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ClubService } from 'src/app/core/services/club.service';
import { CreateClubDialogComponent } from '../create-club-dialog/create-club-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Club } from 'src/app/core/models/club.model';
import { DashboardService } from 'src/app/core/services/dashboard.service';

@Component({
  selector: 'app-organizer-dashboard',
  templateUrl: './organizer-dashboard.component.html',
  styleUrls: ['./organizer-dashboard.component.scss']
})
export class OrganizerDashboardComponent implements OnInit {
  myClubs: Club[] = [];
  isLoading = true;
  stats: any = { totalEvents: 0, upcomingEvents: 0, totalRegistrations: 0, totalClubs: 0 };

  constructor(
    private clubService: ClubService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private dashboardService: DashboardService
  ) {}

  ngOnInit(): void {
    this.loadClubs();
    this.loadStats(); 
  }

  loadClubs() {
    this.isLoading = true;
    
    this.clubService.getMyClubs().subscribe({
      next: (data) => {
        this.myClubs = data;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.snackBar.open('Failed to load clubs', 'Close');
      }
    });
  }

  openCreateClubDialog() {
    const dialogRef = this.dialog.open(CreateClubDialogComponent, {
      width: '500px',
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'success') {
        this.loadClubs(); 
      }
    });
  }

  loadStats() {
    this.dashboardService.getOrganizerStats().subscribe({
      next: (data) => this.stats = data,
      error: (err) => console.error('Failed to load stats', err)
    });
  }
}