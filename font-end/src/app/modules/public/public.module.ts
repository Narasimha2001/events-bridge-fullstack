import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PublicRoutingModule } from './public-routing.module';
import { SharedModule } from 'src/app/shared/shared.module'; // Import this
import { EventListComponent } from './components/event-list/event-list.component';
import { EventDetailsComponent } from './components/event-details/event-details.component';
import { MyRegistrationsComponent } from './components/my-registrations/my-registrations.component';
import { EventSearchBarComponent } from './components/event-search-bar/event-search-bar.component';
import { HomeComponent } from './components/home/home.component';

@NgModule({
  declarations: [
    EventListComponent,
    EventDetailsComponent,
    MyRegistrationsComponent,
    EventSearchBarComponent,
    HomeComponent
  ],
  imports: [
    CommonModule,
    PublicRoutingModule,
    SharedModule
  ]
})
export class PublicModule { }