export interface Event {
  id: number;
  title: string;
  description: string;
  location: string;
  capacity: number;
  status: 'DRAFT' | 'PUBLISHED' | 'CANCELLED' | 'COMPLETED';
  bannerImageUrl?: string;
  clubId: number;
  startTime: string; // ISO String
  endTime: string;   // ISO String
}

export interface EventSummary {
  id: number;
  title: string;
  location: string;
  status: string;
  startTime: string;
  endTime: string;
}

export interface EventSearchFilter {
  title?: string;
  clubId?: number;
  status?: string;
  startDate?: string;
  endDate?: string;
}