import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { catchError, Observable, Subscription, throwError } from 'rxjs';
import { Package } from 'src/app/model/package.model';
import { ParcelService } from 'src/app/service/parcel.service';

@Component({
  selector: 'app-package-info',
  templateUrl: './package-info.component.html',
  styleUrls: ['./package-info.component.css']
})
export class PackageInfoComponent implements OnInit, OnDestroy {
  private id: string | null = null;
  public parcel$: Observable<Package> | undefined;
  // public parcel: Package | undefined;
  // private subscriptions: Subscription[] = [];
  public nonExistentPackage = false;
  public PACKAGE_DOES_NOT_EXIST_MSG = 'Package you are searching does not exist';
  public serverError = false;
  public SERVER_ERROR_MSG = 'Opps! Something went wrong. Please try again later';


  constructor(private route: ActivatedRoute, private parcelService: ParcelService) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('trackingNumber')!;

    this.parcel$ = this.parcelService.trackParcel(this.id)
    .pipe(
      // catch error if occurs
      catchError(
        (error: HttpErrorResponse): Observable<any> => {

            if(error.status === 500 && error.error.message?.includes('Package with provided tracking number not found')){
                this.nonExistentPackage=true;
            }else if(error.status === 0 || error.status === 503 || error.status === 500 ){
              // status=0 - gateway down
              // status=503 - service unavailable
              // status=500 - smth else (like service error-out but not unregistered yet)
                this.serverError = true;
            }
            console.log(error);

            return throwError(() => error);
        },
      )
    );
    // this.subscriptions.push(
    //   this.parcelService.trackParcel(this.id).subscribe({
    //       next: (data: Package) => {
    //         console.log(data);
    //         this.parcel = data;
    //       },
    //       error: (err: HttpErrorResponse) => {
    //         console.log(err);
    //         console.log(err.error.message);
    //       }
    //   })
    // );
  }

  ngOnDestroy(){
    // this.subscriptions.forEach((sub)=> sub.unsubscribe());
  }

}
