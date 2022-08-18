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

  constructor(private http: HttpClient) {}

  public getParcels(page: number, size: number): Observable<any> {
    return this.http.get<any>(
      `${this.getParcelsUrl}/?size=${size}&page=${page}`, {
        observe: 'response',
      }
    ).pipe(map(response => response.body.content as Package[]));
  }

  public getTotalParcels(): Observable<number> {
    // mock
    return new Observable( (obs: Subscriber<number>) => {
      obs.next(10);
      obs.complete();
    })
  }

  // public updateParcel(parcel: Package): Observable<HttpResponse<any>> {
  //   return this.http.post<any>(this.createPackageURL, parcel, {observe: 'response'});
  // }

}
