import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import {MatCheckboxModule} from '@angular/material/checkbox';

const materialComponents = [
  MatButtonModule,
  MatMenuModule,
  MatCardModule,
  MatInputModule,
  MatFormFieldModule,
  MatToolbarModule,
  MatButtonModule,
  MatSidenavModule,
  MatIconModule,
  MatListModule,
  MatCheckboxModule
];


@NgModule({
  declarations: [],
  imports: [materialComponents],
  exports: [materialComponents],
})
export class MaterialModule { }
