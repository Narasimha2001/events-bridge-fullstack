import { Component, OnInit } from '@angular/core';
import { EventSearchFilter } from 'src/app/core/models/event.model';
import { EventService } from 'src/app/core/services/event.service';

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit {
  events: any[] = [];
  isLoading = true;
  currentFilters: EventSearchFilter = {};

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.loadEvents(0);
  }

  loadEvents(pageIndex: number) {
    this.isLoading = true;
    
    this.eventService.searchEvents(this.currentFilters, pageIndex).subscribe({
      next: (response) => {
        this.events = response.content;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading events', err);
        this.isLoading = false;
      }
    });
  }

 onFilterChange(newFilters: EventSearchFilter) {
    this.currentFilters = newFilters; 
    this.loadEvents(0); 
  }

  
}