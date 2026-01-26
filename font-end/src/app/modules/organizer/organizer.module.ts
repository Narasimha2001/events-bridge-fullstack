import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrganizerRoutingModule } from './organizer-routing.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { MatDialogModule } from '@angular/material/dialog';

import { OrganizerDashboardComponent } from './components/organizer-dashboard/organizer-dashboard.component';
import { CreateClubDialogComponent } from './components/create-club-dialog/create-club-dialog.component';
import { ClubManageComponent } from './components/club-manage/club-manage.component';
import { CreateEventDialogComponent } from './components/create-event-dialog/create-event-dialog.component';
import { AttendeesDialogComponent } from './components/attendees-dialog/attendees-dialog.component';
import { EventFormDialogComponent } from './components/event-form-dialog/event-form-dialog.component';

@NgModule({
  declarations: [
    OrganizerDashboardComponent,
    CreateClubDialogComponent,
    ClubManageComponent,
    CreateEventDialogComponent,
    AttendeesDialogComponent,
    EventFormDialogComponent,
    
    ClubManageComponent,
    CreateEventDialogComponent,
    EventFormDialogComponent
  ],
  imports: [
    CommonModule,
    OrganizerRoutingModule,
    SharedModule,
    MatDialogModule
  ]
})
export class OrganizerModule { }