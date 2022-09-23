import { HttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Package } from 'src/app/model/package.model';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { ParcelService } from 'src/app/service/parcel.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { MyPackagesComponent } from './my-packages.component';
import { MatListModule } from '@angular/material/list';

describe('MyPackagesComponent', () => {
  let component: MyPackagesComponent;
  let fixture: ComponentFixture<MyPackagesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MyPackagesComponent],
      imports: [HttpClientTestingModule, MatListModule], // otherwise: NullInjectorError: R3InjectorError(DynamicTestModule [ParcelService -> HttpClient -> HttpClient]: NullInjectorError: No provider for HttpClient!
    }).compileComponents();

    fixture = TestBed.createComponent(MyPackagesComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should exist default 3 hardcoded packages', () => {
    component.packages$.subscribe((packages: Package[]) => {
      expect(packages.length).toBe(3);
    });
  });

});
