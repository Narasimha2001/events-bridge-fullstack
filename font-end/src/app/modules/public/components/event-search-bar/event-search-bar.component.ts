import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-event-search-bar',
  templateUrl: './event-search-bar.component.html',
  styleUrls: ['./event-search-bar.component.scss']
})
export class EventSearchBarComponent {
  @Output() filterChange = new EventEmitter<any>();

  keyword: string = '';
  startDate: Date | null = null;
  endDate: Date | null = null;

 onSearch() {
    this.filterChange.emit({
      title: this.keyword,
      startDate: this.toLocalISOString(this.startDate),
      endDate: this.toLocalISOString(this.endDate)
    });
  }

  toLocalISOString(date: Date | null): string | null {
    if (!date) return null;
    
    const copy = new Date(date);
  
    copy.setMinutes(date.getMinutes() - date.getTimezoneOffset());
    
    return copy.toISOString(); 
  }

  clearFilters() {
    this.keyword = '';
    this.startDate = null;
    this.endDate = null;
    this.onSearch();
  }
}