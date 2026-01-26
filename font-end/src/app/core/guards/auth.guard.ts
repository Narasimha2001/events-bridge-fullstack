import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router
} from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {

    if (this.authService.isLoggedIn()) {

      const requiredRole = route.data['role'];

      if (requiredRole) {
        const userRole = this.authService.getCurrentUser()?.role;
        if (userRole !== requiredRole) {
          this.router.navigate(['/public/events']);
          return false;
        }
      }

      return true;
    }

    this.router.navigate(['/auth/login']);
    return false;
  }
}