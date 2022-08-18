import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  private loginUrl = environment.apiLoginUrl;
  private registerUrl = environment.apiRegisterUrl;
  private refreshUrl = environment.apiRefreshUrl;
  private accessToken: string | null = null;
  private refreshToken: string | null = null;
  private loggedInUsername: string | null = null;
  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) {}

  public login(loginRequest: {
    username: string;
    password: string;
  }): Observable<HttpResponse<any>> {
    return this.http.post<any>(this.loginUrl, loginRequest, {
      observe: 'response',
    });
  }

  public register(registerRequest: {
    username: string;
    password: string;
    firstName: string;
    lastName: string;
  }): Observable<HttpResponse<any>> {
    return this.http.post<any>(this.registerUrl, registerRequest, {
      observe: 'response',
    });
  }

  public logout(): void {
    this.refreshToken = null;
    this.accessToken = null;
    this.loggedInUsername = null;

    // remove items from localStorage - token, user information
    localStorage.removeItem('user');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('accessToken');
  }

  public getAccessToken(refreshToken: string): Observable<HttpResponse<any>> {
    this.isLoggedIn();
    return this.http.post<any>(this.refreshUrl, null, {
      observe: 'response',
      headers: new HttpHeaders({ Authorization: refreshToken }),
    });
  }


  public saveRefreshToken(refreshToken: string): void {
    this.refreshToken = refreshToken;
    localStorage.setItem('refreshToken', refreshToken);
  }

  public saveAccessToken(accessToken: string): void {
    this.accessToken = accessToken;
    localStorage.setItem('accessToken', accessToken);
  }

  public addUsernameToLocalCache(username: string): void {
    this.loggedInUsername = username;
    localStorage.setItem('user', JSON.stringify(username));
  }

  public getUsernameFromLocalCache(): string {
    return JSON.parse(localStorage.getItem('user')!);
  }

  public loadAccessTokenFromLocalCache(): string {
    this.accessToken = localStorage.getItem('accessToken')!;
    return this.accessToken;
  }

  public loadRefreshTokenFromLocalCache(): string {
    this.refreshToken = localStorage.getItem('refreshToken')!;
    return this.refreshToken;
  }

  public isLoggedIn(): boolean {
    // if refresh token is valid - user is logged in
    this.loadRefreshTokenFromLocalCache();
    if (this.refreshToken != null && this.refreshToken !== '') {
      if (this.jwtHelper.decodeToken(this.refreshToken).sub != null || '') {
        if (!this.jwtHelper.isTokenExpired(this.refreshToken)) {
          this.addUsernameToLocalCache(
            this.jwtHelper.decodeToken(this.refreshToken).sub
          );
          return true;
        }
      }
    }
    // if there is no token - logout
    // this.logout(); // getItem from localStorage takes time - immediatelly it will be null - deletes localStorage on every refresh
    return false;
  }

  public isTokenExpired(token: string | undefined) {
    return this.jwtHelper.isTokenExpired(token);
  }
}
