import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { PackageSendComponent } from './package-send.component';

describe('PackageSendComponent', () => {
  let component: PackageSendComponent;
  let fixture: ComponentFixture<PackageSendComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PackageSendComponent],
      imports: [
        HttpClientTestingModule,
        MatFormFieldModule,
        MatCheckboxModule,
        ReactiveFormsModule,
        MatInputModule,
        BrowserAnimationsModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PackageSendComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
