import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import {MatInputModule} from '@angular/material/input';

const materialComponents = [
  MatButtonModule,
  MatMenuModule,
  MatIconModule,
  MatCardModule,
  MatInputModule
];


@NgModule({
  declarations: [],
  imports: [materialComponents],
  exports: [materialComponents],
})
export class MaterialModule { }