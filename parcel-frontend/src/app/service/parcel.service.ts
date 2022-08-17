import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpEvent,
  HttpHeaders,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Package } from '../model/package.model';

@Injectable({ providedIn: 'root' })
export class ParcelService {
  private packagesURL = environment.apiPackagesUrl;
  private packageTrackingURL = environment.apiPackageTrackingUrl;
  private createPackageURL = environment.apiCreatePackageUrl;

  constructor(private http: HttpClient) {}

  public trackParcel(trackingNumber: string): Observable<Package> {
    return this.http.get<Package>(this.packageTrackingURL+trackingNumber);
  }

  public getParcels(): Observable<any> {
    return this.http.get<any>(this.packagesURL);
  }

  public getParcels2(page: number, size: number): Observable<any> {
    return this.http.get<any>(
      `${this.packagesURL}/?size=${size}&page=${page}`
    );
  }

  public createNewParcel(parcel: Package): Observable<HttpResponse<any>> {
    return this.http.post<any>(this.createPackageURL, parcel, {observe: 'response'});
  }

}

