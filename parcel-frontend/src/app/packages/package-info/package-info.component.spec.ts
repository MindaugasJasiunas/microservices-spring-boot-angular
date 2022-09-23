import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, fakeAsync, flush, TestBed, tick, waitForAsync } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { Address } from 'src/app/model/address.model';
import { Package } from 'src/app/model/package.model';
import { Receiver } from 'src/app/model/receiver.model';
import { Sender } from 'src/app/model/sender.model';
import { ParcelService } from 'src/app/service/parcel.service';

import { PackageInfoComponent } from './package-info.component';

describe('PackageInfoComponent', () => {
  let component: PackageInfoComponent;
  let fixture: ComponentFixture<PackageInfoComponent>;

  let parcelService: ParcelService;
  let spy: jasmine.Spy;

  let pkg: Package;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PackageInfoComponent],
      imports: [/*RouterTestingModule,*/ HttpClientTestingModule, MatCardModule], // otherwise: NullInjectorError: R3InjectorError(DynamicTestModule [ParcelService -> HttpClient -> HttpClient]: NullInjectorError: No provider for HttpClient!
      providers: [
        ParcelService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get(): string {
                  return 'test-tracking-number';
                },
              },
            },
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PackageInfoComponent);
    component = fixture.componentInstance;

    // inject service
    parcelService = fixture.debugElement.injector.get(ParcelService);

    pkg = new Package(null, 'tracking', 'desc', 'new', 12.2, true, false, 2, new Sender('first', 'last', '+44', 'mail@mail', new Address('addr1', '2', 'London', '12B', 'BXE 1240', 'UK')), new Receiver('first', 'last', '+44', 'mail@mail', new Address('addr1', '2', 'London', '12B', 'BXE 1240', 'UK')));
    spy = spyOn(parcelService, 'trackParcel')
      .withArgs('test-tracking-number').and.returnValue(of(pkg));

    fixture.detectChanges();

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should Parcel Service method trackParcel( id ) have been called & parcel set', () => {
    expect(spy).toHaveBeenCalled();
    component.parcel$!.subscribe((data: Package) => {
      expect(data).toBe(pkg);
    })
  });
});
