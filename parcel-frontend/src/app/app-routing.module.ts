import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './authentication/login/login.component';
import { RegisterComponent } from './authentication/register/register.component';
import { MainComponent } from './main/main.component';

const routes: Routes = [
  { path: ``, component: MainComponent, pathMatch: 'full' },
  { path: `login`, component: LoginComponent },
  { path: `register`, component: RegisterComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
