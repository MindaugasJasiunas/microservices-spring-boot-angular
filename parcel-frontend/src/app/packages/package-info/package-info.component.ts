import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
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

  constructor(private route: ActivatedRoute, private parcelService: ParcelService) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('trackingNumber')!;

    this.parcel$ = this.parcelService.trackParcel(this.id);
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
