export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  user: AuthUser;
  token: string; 
}

export interface RegisterRequest {
  email: string;
  fullName: string;
  password: string;
  roleName: 'STUDENT' | 'ORGANIZER'; 
}

export interface AuthUser {
  id: number;
  fullName: string;
  email: string;
  role: string;
}