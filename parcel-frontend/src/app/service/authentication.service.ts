import {
  HttpClient,
  HttpResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  private loginUrl = environment.apiLoginUrl;
  private registerUrl = environment.apiRegisterUrl;

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

}
