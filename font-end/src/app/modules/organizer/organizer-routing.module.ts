import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrganizerDashboardComponent } from './components/organizer-dashboard/organizer-dashboard.component';
import { ClubManageComponent } from './components/club-manage/club-manage.component';

const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: OrganizerDashboardComponent },
  { path: 'club/:id', component: ClubManageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OrganizerRoutingModule { }