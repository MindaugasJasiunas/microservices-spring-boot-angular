import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PackageSendComponent } from './package-send.component';

describe('PackageSendComponent', () => {
  let component: PackageSendComponent;
  let fixture: ComponentFixture<PackageSendComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PackageSendComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PackageSendComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
