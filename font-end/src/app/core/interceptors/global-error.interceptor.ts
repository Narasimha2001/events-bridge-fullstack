import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../services/auth.service';

@Injectable()
export class GlobalErrorInterceptor implements HttpInterceptor {

  constructor(
    private router: Router,
    private snackBar: MatSnackBar,
    private authService: AuthService
  ) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {

        let errorMessage = 'An unknown error occurred!';

        //401 Unauthorized (Token Expired)
        if (error.status === 401) {

          if (!this.router.url.includes('/auth/login')) {
            this.authService.doLogoutCleanup();
            this.router.navigate(['/auth/login']);

            this.snackBar.open('Session expired. Please login again.', 'Close', {
              duration: 4000,
              panelClass: ['error-snackbar']
            });
          }
          return throwError(() => error);
        }

        if (error.error instanceof ErrorEvent) {
          errorMessage = `Network Error: ${error.error.message}`;
        }
        else {
          if (error.error && error.error.message) {
            errorMessage = error.error.message;
          } else {
            errorMessage = `Error ${error.status}: Something went wrong at the server.`;
          }
        }

        this.snackBar.open(errorMessage, 'Close', {
          duration: 3000,
          // panelClass: ['error-snackbar']
        });

        return throwError(() => error);
      })
    );
  }
}