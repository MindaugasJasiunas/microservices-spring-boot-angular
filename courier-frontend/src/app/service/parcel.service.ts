import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, Subscriber } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Package } from '../model/package.model';

@Injectable({
  providedIn: 'root'
})
export class ParcelService {
  private getParcelsUrl = environment.apiPackagesUrl;
  private getParcelsCountUrl = environment.apiPackagesCountUrl;

  constructor(private http: HttpClient) {}

  public getParcels(page: number, size: number): Observable<any> {
    return this.http.get<any>(
      `${this.getParcelsUrl}/?size=${size}&page=${page}`, {
        observe: 'response',
      }
    ).pipe(map(response => response.body.content as Package[]));
  }

  public getTotalParcels(): Observable<number> {
    return this.http.get<number>(
      this.getParcelsCountUrl, {
        observe: 'response',
      }
    ).pipe(map((response: HttpResponse<number>) => {
      return response.body as number;
    }));
  }

  // public updateParcel(parcel: Package): Observable<HttpResponse<any>> {
  //   return this.http.post<any>(this.createPackageURL, parcel, {observe: 'response'});
  // }

}
