export interface Club {
  id: number;
  name: string;
  description: string;
  logoUrl?: string;
  creatorId?: number;
}

export interface CreateClubRequest {
  name: string;
  description: string;
}