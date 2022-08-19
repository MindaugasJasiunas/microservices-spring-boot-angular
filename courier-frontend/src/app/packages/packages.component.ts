import { Component, OnInit } from '@angular/core';
import { Observable, Observer } from 'rxjs';
import { Address } from '../model/address.model';
import { Package } from '../model/package.model';
import { Receiver } from '../model/receiver.model';
import { Sender } from '../model/sender.model';
import { ParcelService } from '../service/parcel.service';

@Component({
  selector: 'app-packages',
  templateUrl: './packages.component.html',
  styleUrls: ['./packages.component.css']
})
export class PackagesComponent implements OnInit {
  currentPage: number = 1;
  pageSize: number = 5;
  itemsTotal: number = 0; // from DB
  packages$: Observable<Package[]>;

  constructor(private parcelService: ParcelService) {
    this.packages$ = this.parcelService.getParcels(
      this.currentPage,
      this.pageSize
    );
    this.parcelService.getTotalParcels().subscribe({
      next: (data: number) => {
        this.itemsTotal = data;
      }
    });
  }

  ngOnInit(): void {}

  getCounter(index: number): number {
    return ((this.currentPage-1) * this.pageSize) + 1 + index;
  }

  pageChanged(event: number) {
    // change current page
    this.currentPage = event;
    // load new items from DB
    this.packages$ = this.parcelService.getParcels(
      this.currentPage-1,
      this.pageSize
    );
  }

  changeItemsPerPage(itemsPerPage: number){
    if(itemsPerPage <= 10){ // defence check to protect server
      this.pageSize = itemsPerPage;
      this.pageChanged(1);
    }
  }
}
