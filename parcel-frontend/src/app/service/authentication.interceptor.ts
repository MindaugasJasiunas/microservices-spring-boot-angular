import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, take, tap, switchMap, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthenticationService } from './authentication.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {}

  // runs before request leaves
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (
      request.url.includes(environment.apiLoginUrl) ||
      request.url.includes(environment.apiRefreshUrl) ||
      request.url.includes(environment.apiRegisterUrl) ||
      request.url.includes(environment.apiPackageTrackingUrl)
    ) {
      // pass request through
      return next.handle(request);
    }

    let jwtToken = this.authService.loadAccessTokenFromLocalCache();
    let modifiedRequest = request.clone({
      setHeaders: { Authorization: `${jwtToken}` },
    });

    if (this.authService.isTokenExpired(jwtToken?.substring(7))) {
      // handle expired JWT before sending request
      this.authService.loadRefreshTokenFromLocalCache();
      const refreshToken = this.authService.loadRefreshTokenFromLocalCache();
      if (!refreshToken) {
        // no refresh token - logout & pass through
        this.authService.logout();
        this.router.navigate(['/login']);
        // return next.handle(request);
        throw throwError(() => new Error('no refresh token')); // dont send request
      }

      return this.authService.getAccessToken(refreshToken).pipe(
        take(1),
        tap((response: HttpResponse<any>) => {
          this.authService.saveAccessToken(
            response.headers.get('authorization')!
          );
        }),
        switchMap((response: HttpResponse<any>) => {
          // transformation operator that maps to an Observable<T>
          // return request with updated access token
          const newRequest = request.clone({
            setHeaders: {
              Authorization: response.headers.get('authorization')!,
            },
          });
          return next.handle(newRequest);
        })
      );
    }

    // pass request through
    return next.handle(modifiedRequest);
  }
}
